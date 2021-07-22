import React from "react";

import {
  Alert,
  StyleSheet,
  Text,
  View,
  TextInput,
  ImageBackground,
  TouchableOpacity,
} from "react-native";

import Parse from "parse/react-native";
import { withTranslation } from "react-i18next";
import "../i18n";

class RestorePassword extends React.Component {
  static navigationOptions = {
    headerShown: false,
  };

  constructor(props) {
    super(props);
    this.state = {
      email: "",
      nameError: "",
    };
  }

  navigateToPage = (page) => {
    this.props.navigation.navigate(page);
  };

  _alert = (title, message, namePage, linkToPage) => {
    Alert.alert(
      title,
      message,
      [
        {
          text: "OK",
          style: "cancel",
        },
      ],
      { cancelable: false }
    );
  };

  resetPassword = () => {
    const { t } = this.props;
    console.log("RESETIIIIK");
    Parse.User.requestPasswordReset(this.state.email)
      .then(() => {
        this._alert(
          "Success",
          "An email was sent to your address."
          // "Log In",
          // "LogInStack"
        );
        this.submitAndClear();
      })
      .catch((error) => {
        alert(t("Provide valid email!"));
      });
  };

  submitAndClear = () => {
    this.setState({
      email: "",
    });
  };

  render() {
    const { t, i18n } = this.props;
    return (
      <View style={styles.container}>
        <ImageBackground
          style={styles.obrazek}
          source={require("../assets/images/pozadie.jpg")}
        >
          <View style={{ flex: 2 }}></View>
          <View style={styles.container2}>
            <Text style={styles.caption}>{t('Reset Password')}</Text>
            <Text style={styles.instructions}>
            {t('Please enter your registration email to get the password reset instructions.')}
            </Text>

            <View style={styles.inputContainer}>
              <TextInput
                style={styles.inputs}
                keyboardType="email-address"
                placeholder={t('Email')}
                value={this.state.email}
                onChangeText={(email) => this.setState({ email })}
              />
            </View>

            <TouchableOpacity
              style={[styles.buttonContainer, styles.resetButton]}
              onPress={() => this.resetPassword()}
            >
              <Text style={styles.resetText}>{t('Send email')}</Text>
            </TouchableOpacity>

            <View style={styles.containerLinksRow}>
              <TouchableOpacity
                style={styles.malyButton}
                onPress={() => this.navigateToPage("LogIn")}
              >
                <Text style={styles.malyButTxt}>{t('Login')}</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.malyButton}
                onPress={() => this.navigateToPage("SignUp")}
              >
                <Text style={styles.malyButTxt}>{t('Sign Up')}</Text>
              </TouchableOpacity>
            </View>
          </View>
          <View style={{ flex: 2 }}></View>
        </ImageBackground>
      </View>
    );
  }
}
export default withTranslation()(RestorePassword);

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
    flex: 4,
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
  resetButton: {
    backgroundColor: "#119da4",
  },
  resetText: {
    color: "#fff",
    fontWeight: "bold",
    fontSize: 20,
  },
  containerLinksRow: {
    marginTop: 50,
    flexDirection: "row",
    justifyContent: "center",
  },
  malyButton: {
    width: "40%",
    backgroundColor: "#fff",
    borderRadius: 25,
    height: 35,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 10,
    borderWidth: 2,
    borderColor: "#119da4",
    marginLeft: 15,
    marginRight: 15,
  },
  malyButTxt: {
    fontSize: 15,
    fontWeight: "bold",
    color: "#119da4",
  },
  cancel: {
    backgroundColor: "red",
  },
  instructions: {
    marginBottom: 20,
    textAlign: "center",
  },
});
