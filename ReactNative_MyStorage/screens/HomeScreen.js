import React from "react";
import {
  Alert,
  Pressable,
  StyleSheet,
  Text,
  View,
  TextInput,
  FlatList,
  TouchableOpacity,
} from "react-native";
import Modal from "react-native-modal";
import { Permissions } from "expo-permissions";
import Icon from "react-native-vector-icons/FontAwesome"; //kniznica s ikonami, zonam ikon na stranke kniznice https://fontawesome.com/v4.7.0/icons/
import Parse from "parse/react-native";
import InputSpinner from "react-native-input-spinner";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import { BarCodeScanner } from "expo-barcode-scanner";

import { withTranslation } from "react-i18next";
import "../i18n";

class HomeScreen extends React.Component {
  static navigationOptions = ({ navigation }) => {
    console.info(">> navigation zaciatok");
    return {
      //horna cast nazov a logout
      headerTitle: "Storeroom",
      headerRight: () => (
        <View style={{ flexDirection: "row", flex: 1, marginRight: 15 }}>
          <Icon
            style={{ marginRight: 15 }}
            name="home"
            size={30}
            color="#00b5ec" //nazov ikonu je z kniznice
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
              navigation.navigate("LogoutStack");
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

    const kod = props.route.params.kod;

    this.state = {
      dialogVisible: false,
      dialogVisible2: false,
      modalVisible: false,
      showbar: false,
      nameError: false,
      iconname: "search",
      item: "",
      item_barcode: "",
      item_current: 0,
      item_target: 0,
      item_tobuy: 0,
      scanned: false, //KAMERA
      hasCameraPermission: null, //KAMERA
      barcodeData: "",
      barcodeType: "",
      sorting: "name",
      hladane: "",
      editScannerVisible: false,
      addScannerVisible: false,
    };

    console.info(">> constructor koniec");
  }

  componentDidMount() {
    this.readAllItems();
    this.skenuj();
    this.getPermissionsAsync();
    const { navigation } = this.props;

    this.focusListener = navigation.addListener("focus", () => {
      this.readAllItems();
      this.skenuj();
    });
  }

  skenuj = async () => {
    const { status } = await BarCodeScanner.requestPermissionsAsync();
    setTimeout(() => this.setState({ hasCameraPermission: status === "granted" }), 1000);
    // this.setState({ hasCameraPermission: status === "granted" });
    console.log(this.state.hasCameraPermission);
    if (status !== "granted") {
      setErrorMsg("Permission to access camera was denied");
      return;
    }
  };

  componentWillUnmount() {
    if (this.focusListener != null && this.focusListener.remove)
      this.focusListener.remove();
  }

  getPermissionsAsync = async () => {
    const { status } = await Permissions.askAsync(Permissions.CAMERA);
    this.setState({ hasCameraPermission: status === "granted" });
  };

  navigateToPage = (page) => {
    console.info(">> navigate to page zaciatok");
    this.props.navigation.navigate(page);
  };
  //////////////////////////////////////////////////// FUNKCIE
  _alert = (title, message, namePage, linkToPage) => {
    Alert.alert(
      title,
      message,
      [
        {
          text: "Ok",
          onPress: () => console.log("Cancel Pressed"),
          style: "cancel",
        },
        {
          text: `Back to ${namePage} page`,
          onPress: () => {
            this.navigateToPage(linkToPage);
          },
        },
      ],
      { cancelable: false }
    );
  };

  _onPress = (id) => {
    this.setState({ idObject: id });
    const { t } = this.props;
    Alert.alert(
      t("Edit Item"),
      "",
      [
        {
          text: t("Delete"),
          onPress: () => {
            this._onPressDeleteObject();
          },
        },
        {
          text: t("Update"),
          onPress: () => this.setState({ dialogVisible: true }),
        },
        {
          text: t("Cancel"),
          onPress: () => console.log("Cancel Pressed"),
        },
      ],
      { cancelable: false }
    );
  };

  _onPressDeleteObject = async () => {
    const query = new Parse.Query("Food");
    const object = await query.get(this.state.idObject);
    try {
      object.destroy();
      this.readAllItems();
    } catch (e) {
      alert(e);
    }
  };

  createItem = () => {
    //vytvorenie tabulky ak neexistuje
    let item = this.state.item;
    console.info(">> createItem zaciatok");
    if (item === "" || item === undefined) {
      this.setState(() => ({ nameError: `Fill the fields correctly.` }));
    } else {
      this.setState({ modalVisible: false });
      const Food = Parse.Object.extend("Food");
      const food = new Food();
      food.set("name", this.state.item);
      food.set("barcode", this.state.item_barcode);
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
          this.readAllItems();
          this.setState({ item: "" }); //zmazanie placeholdera
          this.setState({ item_barcode: "" }); //zmazanie placeholdera
          //this.setState({kod:''});
          this.props.navigation.setParams({ kod: null });
        })
        .catch((error) => {
          alert(error);
          this.setState({ item: "" }); //zmazanie placeholdera
          this.setState({ item_barcode: "" }); //zmazanie placeholdera
        });
    }
    console.info(">> createItem koniec");
  };

  searchItem = async () => {
    let ikona = this.state.iconname;
    let item = this.state.hladane;
    console.log(ikona);
    if (ikona === "times") {
      this.readAllItems();
      this.setState({ hladane: "" });
      this.setState({ iconname: "search" });
    } else {
      console.info(">> searchItem zaciatok");
      if (item === "" || item === undefined) {
        this.setState(() => ({ nameError: `Fill the fields correctly.` }));
      } else {
        this.setState({ iconname: "times" });
        const idUser = await Parse.User.currentAsync();
        const query = new Parse.Query("Food");
        query.exists("name");
        query.equalTo("userId", idUser);
        query.matches("name", this.state.hladane);
        //query.addAscending(this.state.sorting);

        const resultQuery = await query.find();
        this.setState({ results: resultQuery });
      }
    }
    console.info(">> createItem koniec");
  };

  readAllItems = async () => {
    const idUser = await Parse.User.currentAsync();
    const query = new Parse.Query("Food");
    query.exists("name");
    query.equalTo("userId", idUser);

    //query.addAscending(this.state.sorting);

    const resultQuery = await query.find();
    this.setState({ results: resultQuery });
  };

  updateItem = () => {
    console.info(">> updateItem zaciatok");

    this.setState({ hladane: "" });
    this.setState({ iconname: "search" });

    const query = new Parse.Query("Food");
    query.get(this.state.idObject).then((object) => {
      object.set("name", this.state.updateValueDialog);
      object.set("barcode", this.state.updateValueDialog2);
      // object.set("target", parseInt(this.state.updateValueDialog3));
      object.set("current", parseInt(this.state.updateValueDialog3));
      object.set("tobuy", parseInt(this.state.updateValueDialog4));
      object.set(
        "kod",
        Parse.User.current().getUsername() +
          this.state.updateValueDialog +
          this.state.updateValueDialog2
      );
      object.save().then(() => {
        this.setState({ updateValueDialog: "", dialogVisible: false });
        this.setState({ updateValueDialog2: "", dialogVisible: false });
        this.setState({ updateValueDialog3: 0, dialogVisible: false });
        this.setState({ updateValueDialog4: 0, dialogVisible: false });
        this.setState({ updateValueDialog5: 0, dialogVisible: false });
        this.readAllItems();
      });
    });

    console.info(">> updateItem koniec");
  };

  deleteIten = async () => {
    const query = new Parse.Query("Food");
    const object = await query.get(this.state.idObject);
    try {
      object.destroy();
      this.getAllData();
    } catch (e) {
      alert(e);
    }
  };

  renderItem = ({ item }) => {
    return (
      <TouchableOpacity //nahradit Pressable
        style={styles.borderFlatListItems}
        onPress={() => {
          this._onPress(item.id);
          this.setState({ updateValueDialog: item.get("name") });
          this.setState({ updateValueDialog2: item.get("barcode") });
          this.setState({ updateValueDialog3: item.get("current") });
          this.setState({ updateValueDialog4: item.get("tobuy") });
        }}
      >
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
      </TouchableOpacity>
    );
  };

  // FUNKCIA NA PRIDANIE NASKENOVANEHO BARCODE DO EDIT MODALU
  handleBarCodeScanned = ({ data }) => {
    alert(`Bar code ${data} has been scanned!`);
    this.setState({ editScannerVisible: false });
    this.setState({ updateValueDialog2: data, dialogVisible: true });
  };

  handleBarCodeScanned2 = ({ data }) => {
    alert(`Bar code ${data} has been scanned!`);
    this.setState({ addScannerVisible: false });
    this.setState({ item_barcode: data, nameError: null, modalVisible: true });
  };

  /////////////////////////////////////////////////////////////////////////   RENDER
  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <View style={styles.header}>
          <Text style={styles.caption}>{t("My Food Storage")}</Text>
          <View style={styles.searchComponent}>
            <View style={{ flexDirection: "row" }}>
              <TextInput
                style={styles.inputName}
                placeholder={t("Search item....")}
                underlineColorAndroid="transparent"
                ref={"inputObject"}
                value={this.state.hladane}
                onChangeText={(text) =>
                  this.setState({ hladane: text, nameError: null })
                }
              />
              <Icon
                name={this.state.iconname}
                style={styles.ikonySrch}
                onPress={() => this.searchItem()}
              />
            </View>
          </View>
        </View>
        {/* 
        {!!this.state.nameError && (
          <View styles={styles.divError}>
            <Text style={styles.divErrorFont}>{this.state.nameError}</Text>
          </View>
        )} */}

        {/* &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&TABULKA NA VECI Z DB */}

        <View style={styles.tabulkaComponent}>
          <View style={styles.tabulkaKategorie}>
            <View style={{ flexDirection: "row" }}>
              <Pressable
                style={{ flex: 3, alignItems: "center" }}
                onPress={() => {
                  this.setState({ sorting: "tobuy" });
                }}
              >
                {({}) => (
                  <Text
                    style={{
                      fontWeight: "bold",
                      color: "#119da4",
                      fontSize: 24,
                    }}
                  >
                    {t("Food")}
                  </Text>
                )}
              </Pressable>

              <View style={{ flex: 3, alignItems: "center" }}>
                <Text
                  style={{ fontWeight: "bold", color: "#119da4", fontSize: 24 }}
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
            // numColumns={1}
            keyExtractor={(item, index) => index.toString()}
          />
        </View>

        <View style={styles.addFood}>
          <Icon
            name="plus-circle"
            color="white"
            size={60}
            style={styles.shadow}
            onPress={() => this.setState({ modalVisible: true })}

            // onPress={() => console.log("chichi")}
          />
        </View>

        {/* xxx###########################################MODAL SO STAVOM MODALVISIBLE NA PRIDANIE JEDLA  NOVEHO */}

        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.modalVisible}
          transparent={true}
          title={t("Add Food to Storage")}
          onTouchOutside={() => this.setState({ modalVisible: false })}
        >
          <View style={styles.modal}>
            <Text style={styles.nadpisikVelky}>{t("Add Food to Storage")}</Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "center",
                paddingBottom: 20,
              }}
            >
              <Text style={styles.nadpisiky}>{t("Name")}:</Text>
              <TextInput
                style={styles.inputNamePad}
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
              <Text style={styles.nadpisiky}>{t("Barcode")}:</Text>

              <TextInput
                style={styles.inputName}
                placeholder="245790756534"
                underlineColorAndroid="transparent"
                ref={"inputObject"}
                value={this.state.item_barcode}
                onChangeText={(text) =>
                  this.setState({ item_barcode: text, nameError: null })
                }
              />

              <Icon
                type="Feather"
                name="barcode"
                style={{ color: "black", fontSize: 24, paddingRight: 20 }}
                onPress={() => this.setState({ addScannerVisible: true })}
              />
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

        {/* 
        MODAL NA EDITACIU TOVARU V SPAJZI */}
        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.dialogVisible}
          transparent={true}
          title="Update Item in Storage"
          onTouchOutside={() => this.setState({ dialogVisible: false })}
        >
          <View style={styles.dialog}>
            <Text style={styles.nadpisikVelky}>{t("Edit Item")}</Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "center",
                alignItems: "center",
                paddingBottom: 20,
              }}
            >
              <Text style={styles.nadpisiky}>{t("Name")}:</Text>
              <TextInput
                style={styles.inputNamePad}
                placeholder="Rename item...."
                underlineColorAndroid="transparent"
                ref={"inputObject"}
                value={this.state.updateValueDialog}
                onChangeText={(text) =>
                  this.setState({
                    updateValueDialog: text,
                    nameErrorUpdate: null,
                  })
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
              <Text style={styles.nadpisiky}>{t("Barcode")}:</Text>

              <TextInput
                style={styles.inputName}
                placeholder="245790756534"
                underlineColorAndroid="transparent"
                ref={"inputObject"}
                value={this.state.updateValueDialog2}
                onChangeText={(text) =>
                  this.setState({
                    updateValueDialog2: text,
                    nameErrorUpdate: null,
                  })
                }
              />

              <Icon
                type="Feather"
                name="barcode"
                style={{ color: "black", fontSize: 24, paddingRight: 20 }}
                onPress={() => this.setState({ editScannerVisible: true })}
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
              <Text style={styles.nadpisiky}>{t("Current Amount")}: </Text>
              <InputSpinner
                max={100}
                min={0}
                step={1}
                colorMax={"#f04048"}
                colorMin={"#119da4"}
                value={this.state.updateValueDialog3}
                onChange={(num) => {
                  this.setState({
                    updateValueDialog3: num,
                    nameErrorUpdate: null,
                  });
                }}
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
              <Text style={styles.nadpisiky}>{t("Need to buy")}:</Text>
              <InputSpinner
                max={100}
                min={0}
                step={1}
                colorMax={"#f04048"}
                colorMin={"#119da4"}
                value={this.state.updateValueDialog4}
                onChange={(num) => {
                  this.setState({
                    updateValueDialog4: num,
                    nameErrorUpdate: null,
                  });
                }}
              />
            </View>

            {/* BUTTONIKY NA SAVE CANCEL  pri update item*/}
            <View style={{ flexDirection: "row" }}>
              <TouchableOpacity
                style={styles.buttonikGrey}
                onPress={() => this.setState({ dialogVisible: false })}
              >
                <Text style={styles.buttonTextGrey}>{t("Cancel")}</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.buttonik}
                onPress={() => this.updateItem()}
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

            {/* {!!this.state.nameErrorUpdate && (
              <View styles={styles.divError}>
                <Text style={styles.divErrorFont}>
                  {this.state.nameErrorUpdate}
                </Text>
              </View>
            )} */}
          </View>
        </Modal>

        {/* MODAL NA BARCODE SCANNER PRI EDITE */}

        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.editScannerVisible}
          transparent={true}
          title="Add Food to Storage"
          onTouchOutside={() => this.setState({ editScannerVisible: false })}
        >
          <View style={styles.scanner}>
            <View style={styles.modalScannerHeader}>
              <MaterialCommunityIcons
                name="close-circle"
                color="#119da4"
                size={40}
                onPress={() => this.setState({ editScannerVisible: false })}
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

        {/* MODAL NA BARCODE SCANNER PRI add food */}

        <Modal
          style={{ backgroundColor: "rgba(0,0,0,0.5)", margin: 0 }}
          visible={this.state.addScannerVisible}
          transparent={true}
          title="Add Food to Storage"
          onTouchOutside={() => this.setState({ addScannerVisible: false })}
        >
          <View style={styles.scanner}>
            <View style={styles.modalScannerHeader}>
              <MaterialCommunityIcons
                name="close-circle"
                color="#119da4"
                size={40}
                onPress={() => this.setState({ addScannerVisible: false })}
              />
            </View>
            <View style={styles.modalScanner}>
              <BarCodeScanner
                onBarCodeScanned={
                  this.state.scanned ? undefined : this.handleBarCodeScanned2
                }
                style={StyleSheet.absoluteFillObject}
              />
            </View>
          </View>
        </Modal>
      </View>
    );
  }
}
export default withTranslation()(HomeScreen);
////////////////////////////////////////////////////////////////////////// STYLY

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
  searchComponent: {
    flex: 1,
    flexDirection: "column",
    backgroundColor: "#FFFFFF",
    borderRadius: 25,
    marginBottom: 15,
    width: "90%",
    borderWidth: 2,
    borderColor: "#119da4",
    alignItems: "center",
    justifyContent: "center",
  },
  caption: {
    flex: 1.5,
    margin: 15,
    fontSize: 40,
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
  containerInputs: {
    // flexDirection: "row",
    justifyContent: "center",
    marginTop: 10,
    borderWidth: 1,
    borderColor: "#00b5ec",
  },

  inputName: {
    paddingLeft: 20,
    flex: 2,
  },
  inputNamePad: {
    paddingLeft: 0,
    flex: 2,
  },
  ikonySrch: {
    color: "lightgrey",
    fontSize: 24,
    flex: 0.3,
  },
  icon: {
    color: "grey",
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    margin: 16,
    fontSize: 24,
  },
  icon2: {
    color: "grey",
    //flexDirection: 'row',
    justifyContent: "center",
    alignItems: "center",
    margin: 16,
    fontSize: 50,
    color: "#00b5ec",
  },
  titleItems: {
    fontWeight: "bold",
    fontSize: 16,
    marginBottom: 5,
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
  dialog: {
    backgroundColor: "white",
    height: 400,
    width: "80%",
    marginBottom: "auto",
    marginTop: 250,
    borderRadius: 10,
    padding: 10,
    alignSelf: "center",
  },
  shadow: {
    // shadowOpacity: 0.4,
    textShadowRadius: 1,
    textShadowOffset: { width: 1.5, height: 2 },
  },
  buttonText: {
    fontSize: 15,
    fontWeight: "bold",
    color: "white",
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
  nadpisiky: {
    fontWeight: "bold",
    fontSize: 16,
    color: "black",
    flex: 1.5,
  },
  nadpisikVelky: {
    fontWeight: "bold",
    paddingBottom: 25,
    textAlign: "center",
    fontSize: 20,
    color: "black",
  },
});
