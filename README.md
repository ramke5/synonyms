

<b>Synonym register tool</b>

This is a Rest API application created in Java using Spring boot framework.
Application stores words with their synonyms.
For example, if "wash" is a synonym to "clean", then the API should return “clean” as result when we ask for the “wash” synonyms and vice versa. 
The register supports transitive rule too. It assumes the following implementation, i.e. if "B" is a synonym to "A" and "C" a synonym to "B", then "C" should automatically, by transitive rule, also be the synonym for "A".
A word may have multiple synonyms.

It has 6 main endpoints:
<br>/get
<br>/post
<br>/edit
<br>/delete
<br>/assign
<br>/deassign

Endpoint can be tested using Postman collection located in <b>src/test/postman</b>.
Application is dockerized which means you can easily run it on your local machine.

Steps that need to be followed:
1. Clone repository
2. Import to IDE
3. mvn install project (to get JAR created)
4. docker-compose up

Now application should be started and running on localhost, port 8080.

Application can be run from IDE too, but in that case you would have to have MySQL server up and running and you would need to create database manually (name specified in application.properties). Tables would get auto created using Hibernate.

As a part of this application, there is one more endpoint (<b>/auth</b>) used for authentication and authorization.
By using this endpoint, user sends his credentials and scope he wants to check, and if all parameters match - as a response Bearer access token, together with scope is received.
This information is retrieved from Okta, and it is compliant to OAuth2.0 standard.

Two test users are previously created and 'reader' scope is assigned to one, and 'writer' scope to another.  
This endpoint can be tested using previously mention collection in resources folder. Credentials can be found there. In addition to this, some unit tests are written. For easier understing of code logic, some comments are left too.

<b>Next steps:</b>
As a result of a lack time, this received token is not used fully to support two level of authorization: reader (only can perform the fetch action) and writer (has unrestricted access).
I assume that particular configuration change needs to happen on Okta side, which would confirm that token is valid and that one of those 6 endpoints can be called. I am planning to figure it out as soon as I get some time.
Another good impovement would be connecting to some API to get information if entered words are real synonyms, and if not - prevent them from saving.

If there are any issues, please feel free to contact me.

