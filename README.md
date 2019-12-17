# Mock Server

It used to mock the application service.

## Installation

1. Clone the repository: `git clone https://code.devsnc.com/benedict-johnson/mockserver`
2. Import the application into : `Eclipse` or `Intellij` as maven project

## Run 

1. Run start.sh from the root folder

## Prerequisites

1. Jdk 1.8 (https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
2. Maven 3 above (https://maven.apache.org/download.cgi)
3. Mongo 3 above (brew install mongo db)
4. Redis (brew install redis)
5. Apache Tomcat

## Swagger

http://localhost:8080/swagger-ui.html

## Tools & Utility
1. Spring Boot
2. Lombok
3. Logback
4. swagger
5. redis
6. mongo

## HTTP

### Methods
GET
HEAD
POST
PUT
DELETE
CONNECT
OPTIONS
TRACE

## Response

### 1xx Informational response
100 Continue
101 Switching Protocols
102 Processing

### 2xx Success
200 OK
201 Created
202 Accepted
203 Non-Authoritative Information
204 No Content
205 Reset Content
206 Partial Content
207 Multi-Status
208 Already Reported
226 IM Used

### 3xx Redirection
300 Multiple Choices
301 Moved Permanently
302 Found
303 See Other
304 Not Modified
305 Use Proxy 
307 Temporary Redirect
308 Permanent Redirect

### 4xx Client errors
400 Bad Request
401 Unauthorized
402 Payment Required
403 Forbidden
404 Not Found
405 Method Not Allowed
406 Not Acceptable
407 Proxy Authentication Required 
408 Request Timeout
409 Conflict
410 Gone
411 Length Required
412 Precondition Failed
413 Payload Too Large
414 URI Too Long 
415 Unsupported Media Type
416 Range Not Satisfiable 
417 Expectation Failed
418 I'm a teapot
421 Misdirected Request 
422 Unprocessable Entity
423 Locked
424 Failed Dependency
426 Upgrade Required
428 Precondition Required
429 Too Many Requests
431 Request Header Fields Too Large
451 Unavailable For Legal Reasons

### 5xx Server errors
500 Internal Server Error
501 Not Implemented
502 Bad Gateway
503 Service Unavailable
504 Gateway Timeout
505 HTTP Version Not Supported
506 Variant Also Negotiates 
507 Insufficient Storage 
508 Loop Detected
510 Not Extended
511 Network Authentication Required

## Headers

### Request
A-IM
Accept
Accept-Charset
Accept-Encoding
Accept-Language
Accept-Datetime
Access-Control-Request-Method
Authorization
Cache-Control
Connection
Content-Length
Content-MD5
Content-Type
Cookie
Date
Expect
Forwarded
From
Host
If-Match
If-Modified-Since
If-None-Match
If-Range
If-Unmodified-Since
Max-Forwards
Origin
Pragma
Proxy-Authorization
Range
Referer
TE
User-Agent
Upgrade
Via
Warning

### Response
Accept-Patch
Accept-Ranges
Access-Control-Allow-Origin
Access-Control-Allow-Credentials
Access-Control-Expose-Headers
Access-Control-Max-Age
Access-Control-Allow-Methods
Access-Control-Allow-Headers
Age
Allow
Alt-Svc
Cache-Control
Connection
Content-Disposition
Content-Encoding
Content-Language
Content-Length
Content-Location
Content-MD5
Content-Range
Content-Type
Date
Delta-Base
ETag
Expires
IM
Last-Modified
Link
Location
P3P
Pragma
Proxy-Authenticate
Public-Key-Pins
Retry-After
Server
Set-Cookie
Strict-Transport-Security
Tk
Trailer
Transfer-Encoding
Upgrade
Vary
Via
Warning
WWW-Authenticate
X-Frame-Options