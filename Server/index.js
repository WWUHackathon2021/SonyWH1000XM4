const express = require("express");
const bodyParser = require("body-parser");

const {
  init,
  getProfile,
  getAllLandmarks,
  initUser,
  userExists,
} = require("./database");

const { OAuth2Client } = require("google-auth-library");
const { clientID } = require("./config.json");
const authClient = new OAuth2Client(clientID);

async function verify(idToken) {
  if (idToken) {
    const ticket = await authClient.verifyIdToken({
      idToken,
      audience: clientID,
    });
    const payload = ticket.getPayload();
    return payload;
  } else {
    throw new Error("empty idToken");
  }
}

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
const port = 5000;

app.get("/ping", (req, res) => {
  res.send("Pong!");
});

app.get("/user/:id", (req, res, next) => {
  const { id } = req.params;
  if (id) {
    getProfile(id)
      .then((data) => {
        console.log({ data });
        if (data) {
          res.json(data);
        } else {
          res.status(404).json({ error: "User not found" });
        }
      })
      .catch((err) => {
        next(err);
      });
  } else {
    console.log("id not found");
    res.status(400).send("id is empty");
  }
});

app.get("/landmarks", (req, res, next) => {
  getAllLandmarks()
    .then((landmarks) => {
      res.json(landmarks);
    })
    .catch((err) => {
      next(err);
    });
});

app.post("/login", (req, res, next) => {
  const { token } = req.body;
  if (!token) {
    return res.status(400).json({ error: "token is empty" });
  }
  let id;
  let name;
  verify(token)
    .then((payload) => {
      id = payload.sub;
      name = payload.name;
      userExists(id).then((exists) => {
        if (exists) {
          return res.json({ message: "ok", id });
        } else {
          initUser(id, name)
            .then(() => {
              return res.json({ message: "ok", id });
            })
            .catch((err) => {
              console.log({ err });
              next(err);
            });
        }
      });
    })
    .catch((err) => {
      console.log({ err });
      return res.status(400).json({ error: "Error verifying token" });
    });
});

app.listen(port, () => {
  console.log(`Application listening on http://localhost:${port}`);
  init();
});
