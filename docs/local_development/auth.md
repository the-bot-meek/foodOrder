# Auth

Local auth is done through a service called key-clock this is so the authentication part of the application can be used
by anyone without needing a pk or sk for a oauth provider.

Currently, the only supported auth method is username and password.

Username and password will not need to be secure as it is only used locally for auth.

| Username | Password |
|----------|----------|
| User     | 1234     |
