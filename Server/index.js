const express = require("express");
const bodyParser = require("body-parser");

const {
  init,
  getProfile,
  getAllLandmarks,
  initUser,
  userExists,
} = require("./database");

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
  const { id, name } = req.body;
  if (!id) {
    return res.status(400).json({ error: "id is empty" });
  }
  userExists(id).then((exists) => {
    if (exists) {
      return res.json({ message: "ok" });
    } else {
      if (!name) {
        return res.status(404).json({
          error:
            "id not found, please try again and specify a name to initialize your account",
        });
      } else {
        initUser(id, name)
          .then(() => {
            return res.json({ message: "ok" });
          })
          .catch((err) => {
            next(err);
          });
      }
    }
  });
});

app.listen(port, () => {
  console.log(`Application listening on http://localhost:${port}`);
  init();
});
