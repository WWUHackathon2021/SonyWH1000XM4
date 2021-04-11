# Journey Backend Endpoint Documentation
## Public DNS
The current root level address is http://ec2-54-190-234-6.us-west-2.compute.amazonaws.com. All calls utilize port 5000, so all endpoints will be built, for example: http://ec2-54-190-234-6.us-west-2.compute.amazonaws.com:5000/ping

## Handlers

Most responses should be JSON objects. Errors will return 4/5xx status code along with a JSON message of the form `{ error: "error message" }`

### /ping - GET
Returns 200 and JSON message of form `{ message: "ok" }`

### /landmarks - GET
Returns a list of all possible landmarks in a JSON message in the form of:
```
{
  landmarkID: {
    location: {
      _latitude: 47.123123,
      _longitude: -122.2222,
    },
    name: "Name",
    pictures: ["array", "of", "image", "urls"]
  },
  ...other landmark objects
}
```

### /login - POST
Given the id token received by Google's authentication service, verifies the token and, upon success, returns a JSON message of form `{ message: "ok", id:"yourUserID" }`. The user ID returned from this is important to save for future calls to the API. Be sure the `Content-Type` is set to `application/x-www-form-urlencoded`. The token should be provided in the form data under the `token` parameter.

If the user has never logged in before, this function will initialize the user's profile.

### /user/{id} - GET
Given a user id received from the `/login` handler, returns a JSON object of that user's information of form:
```
{
  displayName: "First Last",
  exp: 1234,
  picture: "URL.to/google/image"
  landmarks: ["array", "of", "landmark", "IDs"],
  achievements: ["array", "of", "achievement", "IDs"] // currently unimplemented
}
```

### /collect/{userID}/{landmarkID} - GET
Given a userID and landmarkID, collects the landmark for the user, incrementing exp and adding the landmark to the user's landmarks. Returns a JSON message in the form `{ message: "ok" }`
