# Continues Integration§


# Why GitHub actions 
The main reason for choosing github was because of its wide support for GitHub actions and Dependabot. This allow us to automate a lot of the CI/DI and dev ops responsibility's.

# Act
Act will allow us to test github actions locally making it easy to debug and develop our CI infrastructure.

# Dependency management
Initially, I had configured Dependabot to manage the dependencies for this repository. However, 
I have decided to transition away from Dependabot, as it does not support upgrading the Micronaut version in my application. 
To address this limitation, I will now use Renovatebot for dependency management, as it offers broader support across various platforms.

See: [Streamline Micronaut + Gradle Updates with Renovate](https://thriving.dev/blog/streamline-micronaut-gradle-updates-with-renovate-1) to see more information about Renovatebot and micronaut