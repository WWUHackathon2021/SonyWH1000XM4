const serviceAccountKey = require("./serviceAccountKey.json");
const { collectExp } = require("./config.json");
const admin = require("firebase-admin");

let db = null;

module.exports = {
  init() {
    if (!db) {
      admin.initializeApp({
        credential: admin.credential.cert(serviceAccountKey),
      });
      db = admin.firestore();
    }
  },

  async getProfile(id) {
    if (id) {
      const userDoc = await db.collection("users").doc(id).get();
      return userDoc.data();
    }
  },

  async initUser(id, displayName) {
    if (id) {
      const userDoc = await db.collection("users").doc(id).get();
      if (!userDoc.exists) {
        const newUser = {
          displayName,
          exp: 0,
          landmarks: [],
          achievements: [],
        };
        db.collection("users")
          .doc(id)
          .set(newUser)
          .catch((err) => {
            throw err;
          });
      } else {
        throw new Error("User already exists");
      }
    }
  },

  async getAllLandmarks() {
    const landmarks = await db.collection("landmarks").get();
    const result = {};
    landmarks.docs.forEach((landmark) => {
      result[landmark.id] = landmark.data();
    });
    return result;
  },

  async getAllAchievements() {
    const achievements = await db.collection("achievements").get();
    const result = {};
    achievements.docs.forEach((achiev) => {
      result[achiev.id] = achiev.data();
    });
    return result;
  },

  async collectLandmark(userID, landID) {
    console.log({ userID, landID });
    if (landID && userID) {
      const landmarkToCollect = await db
        .collection("landmarks")
        .doc(landID)
        .get();
      if (landmarkToCollect.exists) {
        console.log({ landmarkToCollect });
      } else {
        throw new Error("Landmark does not exist");
      }

      const userRef = db.collection("users").doc(userID);
      const userDoc = await userRef.get();
      const { landmarks: existingLandmarks } = userDoc.data();

      console.log({ existingLandmarks });

      if (userDoc.exists) {
        if (!existingLandmarks.includes(landID)) {
          await userRef.update({
            landmarks: admin.firestore.FieldValue.arrayUnion(landID),
            exp: admin.firestore.FieldValue.increment(collectExp),
          });
        } else {
          throw new Error("Landmark already claimed");
        }
      } else {
        throw new Error("User does not exist");
      }
    }
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
