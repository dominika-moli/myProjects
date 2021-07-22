import React from "react";
import Icon from "react-native-vector-icons/FontAwesome";
import Parse from "parse/react-native";

import {
  Text,
  View,
  Alert,
  StyleSheet,
  FlatList,
  TextInput,
  TouchableOpacity,
} from "react-native";
import Modal from "react-native-modal";
import { Picker } from "@react-native-picker/picker";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

import { withTranslation } from "react-i18next";
import "../i18n";
import * as DocumentPicker from "expo-document-picker";
import * as FileSystem from "expo-file-system";

class ShoppingList extends React.Component {
  static navigationOptions = ({ navigation }) => {
    console.info(">> navigation zaciatok");
    return {
      //horna cast nazov a logout
      headerTitle: "Shopping List",
      headerRight: () => (
        <View style={{ flexDirection: "row", flex: 1, marginRight: 15 }}>
          <Icon
            style={{ marginRight: 15 }}
            name="home"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
            onPress={() => {
              navigation.navigate("HomeStack");
            }}
          />
          <Icon
            style={{ marginRight: 15 }}
            name="shopping-cart"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
            onPress={() => {
              navigation.navigate("BarCodeStack");
            }}
          />
          <Icon
            style={{ marginRight: 15 }}
            name="list-ul"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
            onPress={() => {
              navigation.navigate("ShoppingListStack");
            }}
          />
          <Icon
            style={{ marginRight: 15 }}
            name="sign-out"
            size={30}
            color="#ff0000"
            onPress={() => {
              Alert.alert(
                "Sign out",
                "Are you sure do you want to exit?",
                [
                  {
                    text: "Cancel",
                    onPress: () => console.log("Cancel Pressed"),
                    style: "cancel",
                  },
                  {
                    text: "Yes",
                    onPress: () => {
                      navigation.getParam("clearAll");
                      Parse.User.logOut();
                      navigation.navigate("LogInStack");
                    },
                  },
                ],
                { cancelable: false }
              );
            }}
          />
        </View>
      ),
    };
  }; //koniec NAVIGATION //////////////////////////////////////////////////////////////////////////////////////////////////////////

  constructor(props) {
    //lokalne properties
    console.info(">> constructor  shoppinglist zaciatok");
    super(props);
    this.state = {
      item: "",
      item_barcode: "",
      item_current: 0,
      item_target: 0,
      item_tobuy: 0,
      modalVisible: false,
      selectedFile: "",
    };
    console.info(">> constructor shoppinglist koniec");
  }

  componentDidMount() {
    const { navigation } = this.props;

    this.focusListener = navigation.addListener("focus", () => {
      //   this.readAllItems();
    });
  }

  componentWillUnmount() {
    if (this.focusListener != null && this.focusListener.remove)
      this.focusListener.remove();
  }

  renderItem = ({ item }) => {
    return (
      <View style={styles.borderFlatListItems}>
        <View style={{ flexDirection: "row" }}>
          <View style={styles.stlpecJedlo}>
            <Text style={{ textAlign: "center" }}>{item.name}</Text>
          </View>
          <View style={styles.stlpecKupit}>
            <Text
              style={{
                textAlign: "center",
                fontWeight: "bold",
                color: "#D00000",
              }}
            >
              {item.tobuy}
            </Text>
          </View>
        </View>
      </View>
    );
  };

  pickFile = async () => {
    console.log("blud");
    let result = await DocumentPicker.getDocumentAsync({});
    alert(result.uri);
    console.log(result);

    FileSystem.readAsStringAsync(result.uri)
      .then((data) => {
        console.log(data);
        this.csvJSON(data);
      })
      .catch((err) => {
        console.log("​getFile -> err", err);
      });
  };

  csvJSON(csv) {
    var lines = csv.split("\n");

    var result = [];

    var headers = lines[0].split(",");
    headers[0] = "name";
    headers[1] = "tobuy";

    for (var i = 1; i < lines.length - 1; i++) {
      var obj = {};
      var currentline = lines[i].split(",");

      for (var j = 0; j < headers.length; j++) {
        obj[headers[j]] = currentline[j];
      }

      result.push(obj);
    }

    this.setState({ selectedFile: result });
    console.log(this.state.selectedFile);

    var pole = new Array();

    for (let i = 0; i < result.length; i++) {
      if (parseInt(result[i].tobuy) > 0) {
        pole.push(result[i]);
      }
    }
    this.setState({ results: pole });
    console.log(this.state.results);
  }
  readAllItems = async () => {
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    const resultQuery = await query.find();
    this.setState({ results: resultQuery });
  };

  saveToDb = async () => {
    const { t } = this.props;
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    const resultQuery = await query.find();

    for (let i = 0; i < this.state.selectedFile.length; i++) {
      for (let j = 0; j < resultQuery.length; j++) {
        if (this.state.selectedFile[i].name === resultQuery[j].get("name")) {
          console.log(resultQuery[j].get("name"));
          let idecko = resultQuery[j].id;
          let newToBuy = this.state.selectedFile[i].tobuy;
          console.log(newToBuy);
          const query = new Parse.Query("Food");
          query.get(idecko).then((object) => {
            object.set("tobuy", parseInt(newToBuy));
            object.save();
          });
          break;
        } else if (j === resultQuery.length - 1) {
          let newToBuy = this.state.selectedFile[i].tobuy;
          console.log(newToBuy);
          console.log("nenašol som");
          let item = this.state.selectedFile[i].name;
          if (item === "" || item === undefined) {
            alert("errooor");
          } else {
            const Food = Parse.Object.extend("Food");
            const food = new Food();
            food.set("name", item);
            food.set("barcode", this.state.item_barcode);
            food.set("target", this.state.item_target);
            food.set("current", this.state.item_current);
            food.set("tobuy", parseInt(newToBuy));
            food.set("userId", Parse.User.current());
            food.set(
              "kod",
              Parse.User.current().getUsername() +
                item +
                this.state.item_barcode
            );
            food
              .save()
              .then(() => {
                this.setState({ item: "" });
                this.props.navigation.setParams({ kod: null });
              })
              .catch((error) => {
                alert(error);
                this.setState({ item: "" }); //zmazanie placeholdera
              });
          }
        }
      }
    }
    alert(t("Shopping list updated!"));
  };

  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.caption}>{t("My Shopping List")}</Text>
          <View style={{ flex: 1.8, flexDirection: "row" }}>
            <MaterialCommunityIcons
              name="file-import-outline"
              color="#f8f9fa"
              size={50}
              style={{ alignItems: "center" }}
              onPress={() => this.pickFile()}
            />
          </View>
        </View>

        <View style={styles.tabulkaComponent}>
          <View style={styles.tabulkaKategorie}>
            <View style={{ flexDirection: "row" }}>
              <View style={{ flex: 3, alignItems: "center" }}>
                <Text
                  style={{ fontWeight: "bold", color: "#119da4", fontSize: 24 }}
                >
                  {t("Food")}
                </Text>
              </View>

              <View style={{ flex: 2, alignItems: "center" }}>
                <Text
                  style={{ fontWeight: "bold", color: "#119da4", fontSize: 24 }}
                >
                  {t("Count")}
                </Text>
              </View>
            </View>
          </View>

          <FlatList
            style={{ marginTop: 5, backgroundColor: "white", flex: 6 }}
            data={this.state.results}
            renderItem={this.renderItem}
            showsVerticalScrollIndicator={true}
            keyExtractor={(item, index) => index.toString()}
          />
        </View>

        <View style={styles.addFood}>
          <TouchableOpacity>
            <MaterialCommunityIcons
              name="content-save-all"
              color="white"
              size={50}
              style={styles.shadow}
              onPress={() => this.saveToDb()}
            />
          </TouchableOpacity>
        </View>
      </View>
    );
  }
}
export default withTranslation()(ShoppingList);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "black",
  },
  header: {
    flex: 4,
    backgroundColor: "#119da4",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
  },
  borderFlatListItems: {
    padding: 1,
    marginBottom: 2,
    borderRadius: 10,
    height: 40,
    backgroundColor: "#f8f9fa",
    justifyContent: "center",
  },
  stlpecKupit: {
    flex: 2,
  },
  stlpecJedlo: {
    flex: 3,
  },
  caption: {
    flex: 2,
    margin: 15,
    fontSize: 40,
    paddingTop: 10,
    fontWeight: "bold",
    color: "white",
  },
  tabulkaComponent: {
    backgroundColor: "white",
    padding: 5,
    flex: 10,
    justifyContent: "center",
    alignContent: "center",
  },
  tabulkaKategorie: {
    flex: 0.15,
    backgroundColor: "lightgrey",
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
  },
  addFood: {
    flex: 2,
    flexDirection: "row",
    backgroundColor: "#119da4",
    justifyContent: "center",
    alignItems: "center",
  },
  shadow: {
    textShadowRadius: 1,
    textShadowOffset: { width: 1.5, height: 2 },
  },
  modal: {
    backgroundColor: "white",
    height: 250,
    width: "80%",
    marginBottom: "auto",
    marginTop: 250,
    borderRadius: 10,
    padding: 10,
    alignSelf: "center",
  },
  nadpisikVelky: {
    fontWeight: "bold",
    paddingBottom: 25,
    textAlign: "center",
    fontSize: 20,
    color: "black",
  },
  nadpisiky: {
    fontWeight: "bold",
    fontSize: 16,
    color: "black",
    flex: 1,
  },
  inputName: {
    paddingLeft: 20,
    flex: 2,
  },
  buttonTextGrey: {
    fontSize: 15,
    fontWeight: "bold",
    color: "lightgrey",
  },
  buttonikGrey: {
    width: "40%",
    backgroundColor: "#fff",
    borderRadius: 25,
    height: 35,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 15,
    marginBottom: 10,
    borderWidth: 2,
    borderColor: "lightgrey",
    marginLeft: "auto",
    marginRight: "auto",
  },
  buttonik: {
    width: "40%",
    backgroundColor: "#119da4",
    borderRadius: 25,
    height: 35,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 15,
    marginBottom: 10,
    borderWidth: 2,
    borderColor: "#119da4",
    marginLeft: "auto",
    marginRight: "auto",
  },
  buttonText: {
    fontSize: 15,
    fontWeight: "bold",
    color: "white",
  },
});
