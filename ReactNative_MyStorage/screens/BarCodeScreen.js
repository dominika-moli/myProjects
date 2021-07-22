import React from "react";
import {
  Alert,
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  TextInput,
  FlatList,
} from "react-native";
import { BarCodeScanner } from "expo-barcode-scanner";
import { Permissions } from "expo-permissions";
import Icon from "react-native-vector-icons/FontAwesome"; //kniznica s ikonami, zonam ikon na stranke kniznice https://fontawesome.com/v4.7.0/icons/
import Parse from "parse/react-native";
import Modal from "react-native-modal";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";

import { withTranslation } from "react-i18next";
import "../i18n";

class BarCodeScreen extends React.Component {
  static navigationOptions = ({ navigation }) => {
    console.info(">> navigation zaciatok barscreen");
    return {
      //horna cast nazov a logout
      headerTitle: "Storeroom",
      headerRight: () => (
        // flexDirection je na to aby zobrazilo ikony vedla seba nie default pod sebou
        <View style={{ flexDirection: "row", flex: 1, marginRight: 15 }}>
          <Icon
            style={{ marginRight: 15 }}
            name="home"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
            onPress={() => {
              console.info(">> navigate zaciatok");
              navigation.navigate("HomeStack");
            }}
          />
          <Icon
            style={{ marginRight: 15 }}
            name="shopping-cart"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
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

  ///////////////////////////////////////////////////// CONSTRUCTOR
  constructor(props) {
    //lokalne properties
    console.info(">> constructor zaciatok");
    super(props);
    this.state = {
      scanned: false, //KAMERA
      hasCameraPermission: null, //KAMERA
      barcodeData: "",
      barcodeType: "",
      scannerVisible: false,
      searchedBarcode: "null",
      item: "",
      item_barcode: "",
      item_current: 0,
      item_target: 0,
      item_tobuy: 0,
      tabulka: styles.tabulkaComponent,
      found: false,
      modalVisible: false,
      nameError: false,
    };
    console.info(">> constructor koniec");
  }

  componentDidMount() {
    // this.getPermissionsAsync();
    // this.searchInDb();
    const { navigation } = this.props;
    this.skenuj();

    this.focusListener = navigation.addListener("focus", () => {
      this.setState({ data: "" });
      this.setState({ results: "" });
      this.setState({ tabulka: styles.tabulkaComponent });
      this.skenuj();
    });
  }

  componentWillUnmount() {
    if (this.focusListener != null && this.focusListener.remove)
      this.focusListener.remove();
  }

  skenuj = async () => {
    const { status } = await BarCodeScanner.requestPermissionsAsync();
    this.setState({ hasCameraPermission: status === "granted" });
    console.log(this.state.hasCameraPermission);
    if (status !== "granted") {
      setErrorMsg("Permission to access camera was denied");
      return;
    }
  };

  getPermissionsAsync = async () => {
    const { status } = await Permissions.askAsync(Permissions.CAMERA);
    this.setState({ hasCameraPermission: status === "granted" });
  };

  searchInDb = async () => {
    console.log("searchinf for barcode");
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    const resultQuery = await query.find();

    // var pole = new Array();

    let hladam = this.state.searchedBarcode;
    this.setState({ found: false });
    console.log(hladam);

    for (let i = 0; i < resultQuery.length; i++) {
      if (resultQuery[i].get("barcode") === hladam) {
        this.setState({ found: true });
        console.log(this.state.found);
        let aktualne = parseInt(resultQuery[i].get("current"));
        let kupit = parseInt(resultQuery[i].get("tobuy"));
        let idecko = resultQuery[i].id;
        aktualne = aktualne + 1;

        if (kupit >= 1) {
          kupit = kupit - 1;
        } else {
          kupit = 0;
        }

        const query = new Parse.Query("Food");
        query.get(idecko).then((object) => {
          object.set("current", parseInt(aktualne));
          object.set("tobuy", parseInt(kupit));
          object.save();
        });

        // pole.push(resultQuery[i]);
      }
    }

    if (this.state.found === true) {
      this.updateStavy();
    } else {
      this.showModal();
    }
  };

  showModal = () => {
    this.setState({ item: "" });
    console.log("Barcode sa nenasiel v DB");
    this.setState({ scannerVisible: false });
    this.setState({ modalVisible: true });
  };

  createItem = () => {
    const { t } = this.props;
    let item = this.state.item;
    console.info(">> createItem zaciatok");
    if (item === "" || item === undefined) {
      alert(t(`Fill the fields correctly!`));
    } else {
      this.setState({ modalVisible: false });
      const Food = Parse.Object.extend("Food");
      const food = new Food();
      food.set("name", this.state.item);
      food.set("barcode", this.state.searchedBarcode);
      food.set("target", parseInt(this.state.item_target));
      food.set("current", parseInt(1));
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
          this.setState({ item: "" });
          this.updateStavy();
          this.props.navigation.setParams({ kod: null });
        })
        .catch((error) => {
          alert(error);
          this.setState({ item: "" }); //zmazanie placeholdera
        });
    }
  };

  updateStavy = async () => {
    console.log("updating after changinf pls minus barcode");
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    const resultQuery = await query.find();

    var pole = new Array();

    let hladam = this.state.searchedBarcode;
    console.log(hladam);

    for (let i = 0; i < resultQuery.length; i++) {
      if (resultQuery[i].get("barcode") === hladam) {
        pole.push(resultQuery[i]);
      }
    }

    this.setState({ tabulka: styles.tabulkaComponentVis });
    this.setState({ results: pole });
    this.setState({ searchedBarcode: "null" });
  };

  renderItem = ({ item }) => {
    return (
      <View style={styles.borderFlatListItems}>
        <View style={{ flexDirection: "row" }}>
          <View style={styles.stlpecJedlo}>
            <Text style={{ textAlign: "center" }}>{item.get("name")}</Text>
          </View>

          <View style={styles.stlpecKod}>
            <Text style={{ textAlign: "center" }}>{item.get("barcode")}</Text>
          </View>

          <View style={styles.stlpecPocet}>
            <Text style={{ textAlign: "center" }}>{item.get("current")}</Text>
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

  //////////////////////////////////////////////////// FUNKCIE
  handleBarCodeScanned = ({ data }) => {
    // alert(`Bar code ${data} has been scanned!`);
    setTimeout(() => this.searchInDb(), 1000);
    if (this.state.hasCameraPermission === null) {
      return <Text>Requesting for camera permission</Text>;
    }
    if (this.state.hasCameraPermission === false) {
      return <Text>No access to camera</Text>;
    }
    this.setState({ searchedBarcode: data, scannerVisible: false });
  };

  permission() {
    if (this.state.hasCameraPermission === null) {
      this.setState({ scannerVisible: false });
    }
    if (this.state.hasCameraPermission === false) {
      this.setState({ scannerVisible: false });
    }

    if (this.state.hasCameraPermission === true) {
      this.setState({ scannerVisible: true });
    }
  }
  /////////////////////////////////////////////////////////////////////////   RENDER
  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.caption}>{t("Scan Groceries")}</Text>
        </View>

        <View style={styles.content}>
          <Image
            style={styles.obrazek}
            source={require("../assets/images/scan.png")}
          ></Image>
        </View>

        <View style={styles.buttonik}>
          <TouchableOpacity
            style={styles.buttonContainerW}
            // onPress={() => this.setState({ scannerVisible: true })}

            onPress={() => this.permission()}
          >
            <Text style={styles.buttonTextW}>{t("Start Scanning")}</Text>
          </TouchableOpacity>
        </View>

        <View style={styles.footer}>
          <View style={this.state.tabulka}>
            <View style={styles.tabulkaKategorie}>
              <View style={{ flexDirection: "row" }}>
                <View style={{ flex: 3, alignItems: "center" }}>
                  <Text
                    style={{
                      fontWeight: "bold",
                      color: "#119da4",
                      fontSize: 24,
                    }}
                  >
                    {t("Food")}
                  </Text>
                </View>

                <View style={{ flex: 3, alignItems: "center" }}>
                  <Text
                    style={{
                      fontWeight: "bold",
                      color: "#119da4",
                      fontSize: 24,
                    }}
                  >
                    {t("Barcode")}
                  </Text>
                </View>

                <View style={{ flex: 1, alignItems: "center" }}>
                  <MaterialCommunityIcons
                    name="fridge"
                    color="#119da4"
                    size={28}
                  />
                </View>

                <View style={{ flex: 1, alignItems: "center" }}>
                  <Icon name="shopping-basket" color="#D00000" size={24} />
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
        </View>

        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.scannerVisible}
          transparent={true}
          title="Add Food to Storage"
          onTouchOutside={() => this.setState({ scannerVisible: false })}
        >
          <View style={styles.scanner}>
            <View style={styles.modalScannerHeader}>
              <MaterialCommunityIcons
                name="close-circle"
                color="#119da4"
                size={40}
                onPress={() => this.setState({ scannerVisible: false })}
              />
            </View>
            <View style={styles.modalScanner}>
              <BarCodeScanner
                onBarCodeScanned={
                  this.state.scanned ? undefined : this.handleBarCodeScanned
                }
                style={StyleSheet.absoluteFillObject}
              />
            </View>
          </View>
        </Modal>

        {/* 
MODAL NA PRIDANIE NOVEHO JEDLA PO OSKENOVANI NEZNAMENO BARCODE */}
        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.modalVisible}
          transparent={true}
          title="New in Storage"
          onTouchOutside={() => this.setState({ modalVisible: false })}
        >
          <View style={styles.modal}>
            <Text style={styles.nadpisikVelky}>{t("New In Storage")}</Text>
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
                placeholder={t("Chocolate")}
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
              <Text style={styles.nadpisiky}>{t("Barcode")}: </Text>

              <Text style={styles.inputName}>{this.state.searchedBarcode}</Text>
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
                style={styles.buttonik2}
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

export default withTranslation()(BarCodeScreen);

////////////////////////////////////////////////////////////////////////// STYLY

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "black",
  },
  header: {
    flex: 1.5,
    backgroundColor: "#119da4",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
  },
  caption: {
    margin: 15,
    fontSize: 38,
    fontWeight: "bold",
    color: "white",
  },
  content: {
    flex: 4,
    width: "100%",
    backgroundColor: "white",
    justifyContent: "center",
  },
  buttonik: {
    flex: 1,
    width: "100%",
    backgroundColor: "white",
    justifyContent: "center",
    alignItems: "center",
  },
  footer: {
    flex: 2,
    backgroundColor: "white",
    width: "100%",
  },
  obrazek: {
    width: "80%",
    height: "90%",
    alignSelf: "center",
  },
  scanner: {
    flex: 1,
  },
  modalScanner: {
    height: "100%",
    width: "100%",
    alignSelf: "center",
    // borderRadius: 10,
    flex: 9,
  },
  modalScannerHeader: {
    flex: 1,
    backgroundColor: "white",
    alignItems: "flex-end",
    justifyContent: "center",
    paddingRight: 15,
  },
  buttonContainerW: {
    width: "80%",
    backgroundColor: "white",
    borderRadius: 25,
    height: 50,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 10,
    borderColor: "#119da4",
    borderWidth: 2,
  },
  buttonTextW: {
    fontSize: 20,
    color: "#119da4",
  },
  borderFlatListItems: {
    padding: 1,
    marginBottom: 2,
    borderRadius: 10,
    height: 40,
    backgroundColor: "#f8f9fa",
    justifyContent: "center",
  },
  stlpecJedlo: {
    flex: 3,
  },
  stlpecKod: {
    flex: 3,
  },
  stlpecPocet: {
    flex: 1,
  },
  stlpecKupit: {
    flex: 1,
  },
  tabulkaComponent: {
    backgroundColor: "white",
    padding: 5,
    flex: 10,
    justifyContent: "center",
    alignContent: "center",
    opacity: 0,
  },
  tabulkaComponentVis: {
    backgroundColor: "white",
    padding: 5,
    flex: 10,
    justifyContent: "center",
    alignContent: "center",
    opacity: 1,
  },
  tabulkaKategorie: {
    flex: 0.4,
    backgroundColor: "lightgrey",
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
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
  buttonText: {
    fontSize: 15,
    fontWeight: "bold",
    color: "white",
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
  buttonik2: {
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
  divError: {
    marginTop: 20,
  },
  divErrorFont: {
    textAlign: "center",
    color: "#721c24",
    backgroundColor: "#f8d7da",
    borderColor: "#f5c6cb",
    padding: 20,
    marginTop: 10,
    marginBottom: 10,
    borderWidth: 2,
  },
});
