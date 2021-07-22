import React from "react";
import {
  ImageBackground,
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
} from "react-native";

import { Parse } from "parse/react-native";
import { withTranslation } from "react-i18next";
import "../i18n";

class LogInScreen extends React.Component {
  static navigationOptions = {
    headerShown: false,
  };

  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      nameError: null,
      t: this.props,
    };
  }

  navigateToPage = (page) => {
    this.props.navigation.navigate(page);
  };

  alertAnError = (title, message) => {
    Alert.alert(title, message, [
      {
        text: "OK",
        onPress: () => {
          this.navigateToPage("LogIn");
        },
      },
    ]);
  };

  UNSAFEcomponentWillMount() {
    Parse.User.currentAsync().then((user) => {
      if (user !== undefined || user !== null) {
        this.navigateToPage("LogIn");
      } else {
        let sessionToken = user.getSessionToken();
        Parse.User.become(sessionToken)
          .then(() => {
            this.navigateToPage("Drawer");
          })
          .catch(() => {
            this.navigateToPage("LogIn");
          });
      }
    });
  }

  onLogin = async () => {
    const { t } = this.props;
    let username = this.state.username.trim(),
      password = this.state.password.trim();

    if (username === "" || password === "") {
      // this.setState(() => ({ nameError: `Fill the fields correctly.` }));
      //  alert(t('Fill the fields correctly!'));
      alert(t("Fill the fields correctly!"));
    } else {
      try {
        await Parse.User.logIn(username.toString(), password.toString());
        // this.submitAndClear();
        this.props.navigation.navigate("Drawer");
      } catch (error) {
        // this.setState(() => ({ nameError: error.message }));
        alert(t(`Incorrect name or password!`));
        return error;
      }
    }
  };

  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <ImageBackground
          style={styles.obrazek}
          source={require("../assets/images/pozadie.jpg")}
        >
          <View style={{ flex: 2.5 }}></View>
          <View style={styles.container2}>
            <Text style={styles.caption}>{t("Food Storage")}</Text>
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                keyboardType="email-address"
                placeholder={t("Username")}
                value={this.state.username}
                onChangeText={(username) => this.setState({ username })}
              />
            </View>
            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                placeholder={t("Password")}
                secureTextEntry={true}
                value={this.state.password}
                onChangeText={(password) => this.setState({ password })}
              />
            </View>

            <TouchableOpacity
              style={styles.forgotPas}
              onPress={() => this.navigateToPage("RestorePassword")}
            >
              <Text style={{ color: "#119da4", marginTop: 5 }}>
                {t("Forgot Password?")}
              </Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[styles.buttonContainer, styles.loginButton]}
              onPress={this.onLogin}
            >
              <Text style={styles.loginText}>{t("Login")}</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={() => this.navigateToPage("SignUp")}>
              <View style={{ flexDirection: "row" }}>
                <Text> {t("Don't have an account?")} </Text>
                <Text style={{ color: "#119da4", fontWeight: "bold" }}>
                  {" "}
                  {t("Sign Up")}
                </Text>
              </View>
            </TouchableOpacity>
          </View>
          <View style={{ flex: 2 }}></View>
        </ImageBackground>
      </View>
    );
  }
}
export default withTranslation()(LogInScreen);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#ffffff",
  },
  obrazek: {
    flex: 1,
    resizeMode: "cover",
  },
  container2: {
    alignItems: "center",
    padding: 15,
    marginLeft: "10%",
    marginRight: "10%",
    backgroundColor: "#f8f9fa",
    borderRadius: 10,
    flex: 4,
  },
  caption: {
    marginBottom: 30,
    fontSize: 40,
    fontWeight: "bold",
    color: "#119da4",
  },
  inputContainer: {
    backgroundColor: "#FFFFFF",
    borderRadius: 10,
    height: 50,
    marginBottom: 15,
    width: "100%",
    borderWidth: 1,
    borderColor: "#119da4",
  },
  blud: {
    justifyContent: "center",
    alignItems: "center",
    flex: 1,
    margin: 5,
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
  inputs: {
    height: 50,
    marginLeft: 16,
    color: "black",
  },
  buttonContainer: {
    height: 50,
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 30,
    marginTop: 30,
    borderRadius: 25,
  },
  loginButton: {
    backgroundColor: "#119da4",
  },
  loginText: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 20,
  },
  forgotPas: {
    alignSelf: "flex-end",
  },
});
