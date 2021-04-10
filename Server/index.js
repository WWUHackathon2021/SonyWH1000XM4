const express = require("express");
const { init, testWriteDB, testReadDB } = require("./database");

const app = express();
const port = 5000;

app.get("/", (req, res) => {
  res.send("Hello world!");
});

app.listen(port, () => {
  console.log(`Hello world app listening on http://localhost:${port}`);
  init();
  testReadDB();
});
