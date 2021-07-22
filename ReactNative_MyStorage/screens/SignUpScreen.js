import React from "react";

import {
  StyleSheet,
  Text,
  View,
  TextInput,
  TouchableOpacity,
  ImageBackground,
} from "react-native";

import AsyncStorage from "@react-native-community/async-storage";
import { Parse } from "parse/react-native";
import { withTranslation } from 'react-i18next';
import '../i18n';

 class SignUpScreen extends React.Component {
  static navigationOptions = {
    headerShown: false,
  };

  constructor(props) {
    super(props);
    this.state = {
      username: "",
      password: "",
      nameError: null,
    };
  }

  submitAndClear = () => {
    this.setState({
      username: "",
      password: "",
      nameError: null,
    });
  };

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

  onSignUp = async () => {
    const { t } = this.props;
    let username = this.state.username,
      email = this.state.email,
      password = this.state.password;
    if (
      username.trim() === "" ||
      username === undefined ||
      email.trim() === "" ||
      email === undefined ||
      password.trim() === "" ||
      password === undefined
    ) {
      // this.setState(() => ({ nameError: `Fill the fields correctly.` }));
      alert(t("Fill the fields correctly!"));
    } else {
      try {
        Parse.User.logOut();
        let user = new Parse.User();
        user.set("username", username);
        user.set("email", email);
        user.set("password", password);

        const result = await user.signUp();

        AsyncStorage.setItem("sessionToken", result.getSessionToken());
        AsyncStorage.setItem("username", result.getUsername());
        this.submitAndClear();
        this.navigateToPage("Drawer");
      } catch (error) {
        console.log(error);
        // this.setState(() => ({ nameError: error.message }));
        alert(error);
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
          <View style={{ flex: 2 }}></View>
          <View style={styles.container2}>
            <Text style={styles.caption}>{t('Sign Up')}</Text>
            <Text style={styles.instructions}>{t("Don't have an account? Creating one takes less than a minute and it's free!")}</Text>

            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                keyboardType="default"
                placeholder={t('Username')}
                value={this.state.username}
                onChangeText={(username) => this.setState({ username })}
              />
            </View>

            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                keyboardType="email-address"
                placeholder={t('Email')}
                value={this.state.email}
                onChangeText={(email) => this.setState({ email })}
              />
            </View>

            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                placeholder={t('Password')}
                value={this.state.password}
                secureTextEntry={true}
                underlineColorAndroid="transparent"
                value={this.state.password}
                onChangeText={(password) => this.setState({ password })}
              />
            </View>

            {/* {!!this.state.nameError && (
              <View styles={styles.divError}>
                <Text style={styles.divErrorFont}>{this.state.nameError}</Text>
              </View>
            )} */}

            <TouchableOpacity
              style={[styles.buttonContainer, styles.signupButton]}
              onPress={this.onSignUp}
            >
              <Text style={styles.signupText}>{t('Sign Up')}</Text>
            </TouchableOpacity>

            <TouchableOpacity
                onPress={() => this.props.navigation.navigate("LogIn")}
              >
                <View style={{ flexDirection: "row" , marginTop:10, marginBottom:10}}>
                  <Text>{t('Already have an account?')}</Text>
                  <Text style={{ color: "#119da4", fontWeight: "bold" }}>
                    {" "}
                    {t('Log In')}
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

export default withTranslation()(SignUpScreen);

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
    padding: 18,
    marginLeft: "10%",
    marginRight: "10%",
    flex: 4.7,
    backgroundColor: "#f8f9fa",
    borderRadius: 10,
  },
  caption: {
    marginBottom: 20,
    fontSize: 37,
    fontWeight: "bold",
    color: "#119da4",
  },
  inputContainer: {
    backgroundColor: "#FFFFFF",
    borderRadius: 10,
    height: 50,
    marginBottom: 15,
    flexDirection: "row",
    borderWidth: 1,
    borderColor: "#119da4",
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
    flex: 1,
    color: "black",
  },

  buttonContainer: {
    height: 50,
    width: "100%",
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 5,
    marginTop: 15,
    borderRadius: 25,
  },
  signupButton: {
    backgroundColor: "#119da4",
  },
  signupText: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 20,
  },
  txtLink: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    padding: 15,
  },
  loginText: {
    color: "#fff",
  },
  instructions: {
    marginBottom: 20,
    textAlign: "center",
  },
});
