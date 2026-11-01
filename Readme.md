# Spring Security — notes

## Authentication vs Authorization

**Authentication** is proving who the user is. We take credentials (username/password, token, etc.) and verify them against what we already know. If they match, the user is a known principal in the application. If they do not, the request stops here.

**Authorization** comes after that. We already know who the caller is; now we decide whether that identity is allowed to hit this URL, method, or feature. Authentication answers “who are you?” Authorization answers “are you allowed to do this?”

A user can be authenticated and still get 403. That is expected. Login succeeded; permission did not.

---

## Authentication workflow (request path)


![Spring Security Authentication Workflow](docs/Spring%20Security%20Authentication%20Workflow.png)


---

## Ways to wire this up

### Step 0 — add the dependency

Add `spring-boot-starter-security` dependency in pom. Once this is added and application is restarted, you see similar logs as below:
```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.1.2)

2026-11-01T14:15:20.067+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Starting SpringsecurityApplication using Java 21.0.12.1 with PID 16792 (/Users/modin/Downloads/springsecurity/target/classes started by modin in /Users/modin/Downloads/springsecurity)
2026-11-01T14:15:20.069+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : No active profile set, falling back to 1 default profile: "default"
2026-11-01T14:15:20.489+05:30  INFO 16792 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8080 (http)
2026-11-01T14:15:20.496+05:30  INFO 16792 --- [springsecurity] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-11-01T14:15:20.497+05:30  INFO 16792 --- [springsecurity] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.27]
2026-11-01T14:15:20.519+05:30  INFO 16792 --- [springsecurity] [           main] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 418 ms
2026-11-01T14:15:20.677+05:30  WARN 16792 --- [springsecurity] [           main] .s.a.UserDetailsServiceAutoConfiguration : 

**Using generated security password: a0e46a71-a39a-4eef-90b0-b71c050c63be**

This generated password is for development use only. Your security configuration must be updated before running your application in production.

2026-11-01T14:15:20.696+05:30  INFO 16792 --- [springsecurity] [           main] r$InitializeUserDetailsManagerConfigurer : Global AuthenticationManager configured with UserDetailsService bean with name inMemoryUserDetailsManager
2026-11-01T14:15:20.773+05:30  INFO 16792 --- [springsecurity] [           main] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8080 (http) with context path '/'
2026-11-01T14:15:20.777+05:30  INFO 16792 --- [springsecurity] [           main] c.l.s.SpringsecurityApplication          : Started SpringsecurityApplication in 0.926 seconds (process running for 1.162)

```
This security password will be needed to access the endpoint with the username as "user". This is the default behaviour of Spring Security.

### 1. Users in `application.properties` / How to use custom username and passwords ?

Use this when the user set is tiny and basically static — a couple of admin accounts for a company portfolio site, an internal demo, local bootstrapping. Spring Boot’s default user is this model:

```properties
spring.security.user.name=admin
spring.security.user.password=admin
```

Good for learning and for apps that will never grow a real user table. Weak for anything production: no hashing strategy you control, no roles per user beyond the default, no user management, credentials sitting in config.

### 2. Users in the database / How DB users can login ?

This is the production-grade login path. We implement `UserDetailsService` (or use JDBC/JPA user details), load the account from the DB, and verify with a `PasswordEncoder`. Roles and authorities live with the user record, so authorization can actually vary per account.

Same filter / `AuthenticationManager` / `DaoAuthenticationProvider` flow as above. Only `loadUserByUsername()` changes — it hits the database instead of properties or in-memory users.


----------------------------------------

# How to secure the passwords? Why is it needed?

- If we store passwords in plain text, anyone who gets access to the database can read user credentials and potentially compromise user accounts.
- Password reuse makes this even more dangerous because an attacker might use leaked credentials to access other platforms.
- To avoid this, we should never store plain-text passwords. We need a secure password-storage mechanism.

- How can we secure passwords?
  - Should we encode the password string?
  - Should we encrypt the password string?
  - Should we hash the password string?

Let's understand this one by one.

### Encoding

- It is the process of transforming data from one representation into another representation.
- Multiple encoding schemes are available: Base64, Hexadecimal (Base16), ASCII85, URL Encoding etc.
- This is a reversible process, meaning original data can be retrieved by decoding using the corresponding algorithm.
- Encoding does not require any secret key. Anyone who knows the encoding scheme can decode the data.
- Should we encode the password string?
  - No, because encoding does not provide security or confidentiality. Anyone can decode the password and retrieve the original credentials.
  - For example, Base64 encoding `password123` produces `cGFzc3dvcmQxMjM=`. Anyone can decode it.

### Encryption

- Encryption is the process of converting readable data (plaintext) into an unreadable format (ciphertext) using an encryption algorithm and a cryptographic key.
- The original data can be retrieved by decrypting the ciphertext using the appropriate key.
- Two major types of encryption algorithms are available:

  - **Symmetric Encryption:**
    - Uses the same secret key for encryption and decryption.
    - Example: AES (Advanced Encryption Standard).
    - The secret key must be securely stored and protected.

  - **Asymmetric Encryption:**
    - Uses a pair of cryptographic keys: Public Key and Private Key.
    - Data encrypted with the public key can generally be decrypted using the corresponding private key.
    - Example: RSA, ECC-based encryption schemes.

- Should we encrypt the password string?
  - Generally, no. Passwords do not need to be retrieved by the application; they only need to be verified during login.
  - Encryption is reversible. If an attacker obtains both the encrypted passwords and the decryption key, they can recover the original passwords.
  - It also introduces key-management overhead, including secure storage, rotation and access control.
  - Encryption is useful for sensitive data that must be retrieved later, such as API credentials, payment-related data or confidential database fields.

### Hashing

- Hashing is the process of converting input data into a fixed-format hash using a mathematical algorithm.
- It is a one-way process, meaning the original input cannot practically be recovered from the generated hash.
- Unlike encryption, hashing does not require a decryption key.
- Different hashing algorithms are available:
  - SHA-256
  - SHA-512
  - BCrypt
  - Argon2
  - PBKDF2
  - scrypt

- Should we hash the password string?
  - Yes. Passwords should be stored using dedicated password-hashing algorithms such as BCrypt, Argon2id or PBKDF2.
  - During registration, we generate a password hash and store it in the database instead of the original password.
  - During login, we retrieve the stored hash and verify the incoming password against it.
  - We do not decrypt the hash or compare two newly generated hash strings. We use the algorithm's verification mechanism.

**Important concepts:**

- **Salt:** A randomly generated value added to the password-hashing process before generating the hash.
  - Prevents identical passwords from producing identical stored hashes when different salts are used.
  - Makes precomputed rainbow-table attacks impractical.
  - Salt is stored alongside the hash; it does not need to be secret.

- **Work Factor / Cost Factor:**
  - Controls how computationally expensive the password-hashing operation is.
  - Higher cost means more processing time and resources are required.
  - It should be configured based on the application's performance requirements and current security recommendations.

- **Pepper:**
  - An additional secret value combined with the password before hashing.
  - Unlike salt, pepper is shared across passwords and must be stored separately from the database, preferably in a secrets manager or HSM.
  - It provides an additional layer of protection if the database is compromised, but requires careful rotation and recovery planning.

### BCrypt — How does it work?

- Suppose a user registers with the password `password123`.
- BCrypt generates a random salt and combines it with the password during its hashing process.
- The generated hash, including the salt and cost information, is stored in the database.
- Example:

  `password123` → `$2a$12$...`

- During login:
  - User enters the password.
  - Application retrieves the corresponding BCrypt hash from the database.
  - BCrypt extracts the salt and cost information from the stored hash.
  - It performs the hashing operation using the incoming password and stored parameters.
  - It verifies whether the resulting value matches the stored hash.
  - If matched, authentication succeeds; otherwise, authentication fails.

**Important:** The same password can generate different BCrypt hashes because a new random salt is generated each time. Therefore, we should use `matches()` instead of directly comparing hash strings.