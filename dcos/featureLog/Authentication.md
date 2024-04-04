# Authentication

To get oauth authentication in the micronaut application I followed this guide.
[SECURE A MICRONAUT APPLICATION WITH AUTH0](https://guides.micronaut.io/latest/micronaut-oauth2-auth0-gradle-java.html)

I have decided to switch to Auth0 as the oauth provider for this application since this guide have clear instructions on how to set it up.

After setting up the ping controller to test that the oauth flow is working as expected I kept getting the following error msg during the callback:

```json
{
  "message": "Internal Server Error",
  "_links": {
    "self": {
      "href": "/oauth/callback/auth0?code=LavvhsuNVHLKf5bBbre8yD2wjKVkWT4oycRz4e45FOwOc&state=eyJyZWRpcmVjdFVyaSI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9vYXV0aC9jYWxsYmFjay9hdXRoMCIsIm5vbmNlIjoiYTkxZGUxOTYtNDIxOS00ZjgxLTg4MTMtMDUzYzIyNmIxNjEwIn0%3D",
      "templated": false
    }
  },
  "_embedded": {
    "errors": [
      {
        "message": "Internal Server Error: error: invalid_request, errorDescription: Parameter 'code_verifier' is required, errorUri: null"
      }
    ]
  }
}
```

I took a look online and found these two web pages 

Not very helpful:
[auth0 community 42652](https://community.auth0.com/t/setting-code-verifier-in-the-auth0provider/42652)

I could not find any relent documentation online about the PKCE functionality in Micronaut or Auth0. 
I looked through the micronaut security code and found that I could disable PKCE validation. 
This is not an idea solution but since this application dose not need to be completely secure at the moment it should be fine.


Once I resolved the PKCE issue I started to get this error response and error log when trying to access the secured loggedIn endpoint:
```json
{
  "message": "JWT validation failed",
  "_links": {
    "self": {
      "href": "/oauth/callback/auth0?code=MtYbqk18OoSMXkEZ_ldqFvL9OhnRSIcLbP892nTQHpUEW&state=eyJyZWRpcmVjdFVyaSI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9vYXV0aC9jYWxsYmFjay9hdXRoMCIsIm5vbmNlIjoiMWVjMGVjMTItYTBkOC00ODI0LTg5NjItZjkwYjU5NWJkMWYyIn0%3D",
      "templated": false
    }
  },
  "_embedded": {
    "errors": [
      {
        "message": "JWT validation failed"
      }
    ]
  }
}
```

I found out that this issue was being caused by the fact that Micronaut was not correctly parsing the cookie sent during the callback.
This was being caused by the new DefaultServerCookieDecoder added to micronaut.
This github issue backs this up. I have decided to put the authentication work for this project onto the back burner until this issue has been resolved.
[Cookie Parsing Failure](https://github.com/micronaut-projects/micronaut-core/issues/10435)
