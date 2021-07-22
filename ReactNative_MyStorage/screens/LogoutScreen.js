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
import { withTranslation } from 'react-i18next';
import '../i18n';

class LogoutScreen extends React.Component {
  static navigationOptions = {
    headerShown: false,
  };

  navigateToPage = (page) => {
    this.props.navigation.navigate(page);
  };

  odhlas() {
    Parse.User.logOut();
    this.navigateToPage("LogIn");
  }

  render() {
    const { t } = this.props;
    return (
      <View style={styles.container}>
        <Text style={styles.caption}>{t("Are you sure you want to log out?")}</Text>
        <TouchableOpacity
          style={styles.buttonContainerR}
          onPress={() => this.navigateToPage(t("Home"))}
        >
          <Text style={styles.buttonTextR}>{t("Cancel")}</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={styles.buttonContainerW}
          onPress={() => this.odhlas()}
        >
          <Text style={styles.buttonTextW}>{t("Log Out")}</Text>
        </TouchableOpacity>
      </View>
    );
  }
}

export default withTranslation()(LogoutScreen);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
     backgroundColor: '#1A1A1D'
    // backgroundColor: "lightgrey",
  },
  caption: {
    color: "#119da4",
    fontSize: 35,
    fontWeight: "bold",
    marginBottom: 60,
    textAlign: "center",
    padding: 5,
  },
  buttonContainerR: {
    width: "80%",
    backgroundColor: "#119da4",
    borderRadius: 25,
    height: 50,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 60,
    marginBottom: 15,
  },
  buttonContainerW: {
    width: "80%",
    backgroundColor: "white",
    borderRadius: 25,
    height: 50,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 10,
  },
  buttonTextR: {
    fontSize: 20,
    color: "#E0E1DD",
  },
  buttonTextW: {
    fontSize: 20,
    color: "#119da4",
  },
});
