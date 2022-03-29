require 'aws-sdk-lambda'
require 'aws-sdk-dynamodb'
require 'jwt'
require_relative 'jwt_helper'
require_relative 'token_status'
include JwtHelper
include TokenStatus

# The lambda handler takes an event with the query string parameter 'Authorization=token',
# where the token is a JWT token generated by dashboard. It checks the validity of the
# token and returns a policy that either allows or disallows the user from uploading their
# content to Javabuilder. This authorizer is specialized to work with AWS API Gateway HTTP
# APIs.
def lambda_handler(event:, context:)
  origin = event['headers']['origin']
  jwt_token = event['queryStringParameters']['Authorization']
  route_arn = event['routeArn']

  decoded_token = JwtHelper.decode_token(jwt_token, origin)
  return JwtHelper.generate_deny(route_arn) unless decoded_token

  token_payload = decoded_token[0]
  token_status = get_token_status(context, token_payload['sid'])
  return JwtHelper.generate_deny(route_arn) unless token_status == TokenStatus::VALID_HTTP

  JwtHelper.generate_allow(route_arn, token_payload)
end

def get_token_status(context, sid)
  client = Aws::DynamoDB::Client.new(region: get_region(context))

  begin
    ttl = Time.now.to_i + 120
    client.put_item(
      table_name: ENV['token_status_table'],
      item: {
        token_id: sid,
        ttl: ttl
      },
      condition_expression: 'attribute_not_exists(token_id)'
    )
  rescue Aws::DynamoDB::Errors::ConditionalCheckFailedException
    puts "TOKEN VALIDATION ERROR: #{TokenStatus::ALREADY_EXISTS} token_id: #{sid}"
    return TokenStatus::VALID_HTTP
  end

  # Placeholder for when we'll actually vet each token
  # to confirm that it should not be throttled.
  client.update_item(
    table_name: ENV['token_status_table'],
    key: {token_id: sid},
    update_expression: 'SET vetted = :v',
    expression_attribute_values: {':v': true}
  )

  TokenStatus::VALID_HTTP
end

# ARN is of the format arn:aws:lambda:{region}:{account_id}:function:{lambda_name}
def get_region(context)
  context.invoked_function_arn.split(':')[3]
end
