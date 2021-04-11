const express = require("express");
const { init, getProfile, getAllLandmarks } = require("./database");

const app = express();
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

app.listen(port, () => {
  console.log(`Hello world app listening on http://localhost:${port}`);
  init();
});
