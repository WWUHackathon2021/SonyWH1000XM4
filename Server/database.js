const serviceAccountKey = require("./serviceAccountKey.json");
const admin = require("firebase-admin");

let db = null;

module.exports = {
  init() {
    admin.initializeApp({
      credential: admin.credential.cert(serviceAccountKey),
    });
    db = admin.firestore();
  },

  testWriteDB(obj = { field: "Hello World!" }) {
    db.collection("testColl")
      .add({
        field1: "hello",
        field2: "world",
      })
      .then((docRef) => {
        console.log("Document written!");
        console.log({ docRef });
      })
      .catch((err) => {
        console.log({ err });
      });
  },

  testReadDB(coll = "testColl") {
    db.collection(coll)
      .get()
      .then((snapshot) => {
        console.log({ snapshot });
        snapshot.forEach((doc) => {
          console.log({ doc });
          const [id, data] = [doc.id, doc.data()];
          console.log({ id, data });
        });
      })
      .catch((err) => {
        console.log({ err });
      });
  },
};
