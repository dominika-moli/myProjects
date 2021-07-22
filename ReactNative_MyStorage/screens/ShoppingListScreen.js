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

import { withTranslation } from "react-i18next";
import "../i18n";
import InputSpinner from "react-native-input-spinner";

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
    console.log("willFocus Shopping 0runs");
    this.readAllItems();
    const { navigation } = this.props;

    this.focusListener = navigation.addListener("focus", () => {
      this.readAllItems();
    });
  }

  componentWillUnmount() {
    if (this.focusListener != null && this.focusListener.remove)
      this.focusListener.remove();
  }

  readAllItems = async () => {
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    const resultQuery = await query.find();

    var pole = new Array();

    for (let i = 0; i < resultQuery.length; i++) {
      if (parseInt(resultQuery[i].get("tobuy")) > 0) {
        pole.push(resultQuery[i]);
      }
    }

    this.setState({ results: pole });
    console.log(this.state.results);
  };

  createItem() {
    const { t } = this.props;
    console.log("CREATE ITEM SHOOPING LIST");
    console.log(this.state.item);
    console.log(this.state.item_tobuy);
    let item = this.state.item;
    if (item === "" || item === undefined) {
      alert(t(`Fill the fields correctly!`));
    } else {
      this.setState({ modalVisible: false });
      const Food = Parse.Object.extend("Food");
      const food = new Food();
      food.set("name", this.state.item);
      food.set("barcode", this.state.item_barcode);
      food.set("target", parseInt(this.state.item_target));
      food.set("current", parseInt(this.state.item_current));
      food.set("tobuy", parseInt(this.state.item_tobuy));
      food.set("userId", Parse.User.current());
      food.set(
        "kod",
        Parse.User.current().getUsername() +
          this.state.item +
          this.state.item_barcode
      );
      food
        .save()
        .then(() => {
          this.readAllItems();
          this.setState({ item: "" }); //zmazanie placeholdera
          this.setState({ item_barcode: "" });
          this.setState({ item_tobuy: 0 }); //zmazanie placeholdera
          //this.setState({kod:''});
          this.props.navigation.setParams({ kod: null });
        })
        .catch((error) => {
          alert(error);
          this.setState({ item: "" }); //zmazanie placeholdera
          this.setState({ item_barcode: "" });
          this.setState({ item_tobuy: 0 }); //zmazanie placeholdera
        });
    }
  }

  renderItem = ({ item }) => {
    return (
      <View style={styles.borderFlatListItems}>
        <View style={{ flexDirection: "row" }}>
          <View style={styles.stlpecJedlo}>
            <Text style={{ textAlign: "center" }}>{item.get("name")}</Text>
          </View>
          <View style={styles.stlpecKupit}>
            <Text
              style={{
                textAlign: "center",
                fontWeight: "bold",
                color: "#D00000",
              }}
            >
              {item.get("tobuy")}
            </Text>
          </View>
        </View>
      </View>
    );
  };

  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.caption}>{t("My Shopping List")}</Text>
          <View style={{ flex: 1.8, flexDirection: "row" }}>
            <Icon name="shopping-basket" color="#f8f9fa" size={50} />
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
          <View style={styles.addFood}>
            <Icon
              name="plus-circle"
              color="white"
              size={60}
              style={styles.shadow}
              onPress={() => this.setState({ modalVisible: true })}
            />
          </View>
        </View>

        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.modalVisible}
          transparent={true}
          title="Add Food to Storage"
          onTouchOutside={() => this.setState({ modalVisible: false })}
        >
          <View style={styles.modal}>
            <Text style={styles.nadpisikVelky}>
              {t("Add Item to Shooping List")}
            </Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "center",
                paddingBottom: 20,
              }}
            >
              <Text style={styles.nadpisiky}>{t("Name")}: </Text>
              <TextInput
                style={styles.inputName}
                placeholder="Chocolate"
                underlineColorAndroid="transparent"
                ref={"inputObject"}
                value={this.state.item}
                onChangeText={(text) =>
                  this.setState({ item: text, nameError: null })
                }
              />
            </View>

            <View
              style={{
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "center",
                paddingBottom: 30,
              }}
            >
              <Text style={styles.nadpisiky}>{t("Buy")}: </Text>

              <InputSpinner
                max={100}
                min={0}
                step={1}
                colorMax={"#f04048"}
                colorMin={"#119da4"}
                value={this.state.updateValueDialog4}
                onChange={(num) => {
                  this.setState({
                    item_tobuy:num
                  });
                }}
              />
              {/* <View style={{ marginLeft: 50, marginRight: 80 }}>
                <Picker
                  selectedValue={this.state.item_tobuy}
                  style={{ geight: 50, width: 100 }}
                  onValueChange={(itemValue, itemIndex) =>
                    this.setState({ item_tobuy: itemValue })
                  }
                >
                  <Picker.Item label="1" value="1" />
                  <Picker.Item label="2" value="2" />
                  <Picker.Item label="3" value="3" />
                  <Picker.Item label="4" value="4" />
                  <Picker.Item label="5" value="5" />
                  <Picker.Item label="6" value="6" />
                  <Picker.Item label="7" value="7" />
                  <Picker.Item label="8" value="8" />
                  <Picker.Item label="9" value="9" />
                  <Picker.Item label="10" value="10" />
                </Picker>
              </View> */}
            </View>

            {/* BUTTONIKY NA SAVE CANCEL */}
            <View style={{ flexDirection: "row" }}>
              <TouchableOpacity
                style={styles.buttonikGrey}
                onPress={() => this.setState({ modalVisible: false })}
              >
                <Text style={styles.buttonTextGrey}>{t("Cancel")}</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.buttonik}
                onPress={() => this.createItem()}
              >
                <View
                  style={{
                    flexDirection: "row",
                    justifyContent: "center",
                    alignItems: "center",
                  }}
                >
                  <Text style={styles.buttonText}>{t("Save")}</Text>
                  <Icon
                    name="save"
                    style={{ color: "white", fontSize: 18, paddingLeft: 10 }}
                  />
                </View>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
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
