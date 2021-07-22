import React from "react";
import { StyleSheet, View, Platform, StatusBar } from "react-native";
import AppLoading from 'expo-app-loading';
import keys from "./constants/Keys";
import Parse from "parse/react-native";
import AsyncStorage from "@react-native-community/async-storage";

import MainStackNavigator from './navigation/MainStackNavigator'

Parse.setAsyncStorage(AsyncStorage);

Parse.initialize(keys.applicationId, keys.javascriptKey);
Parse.serverURL = keys.serverURL;

console.disableYellowBox = true;

export default class App extends React.Component {
  state = {
    isLoadingComplete: false,
  };


  render() {
    if (!this.state.isLoadingComplete && !this.props.skipLoadingScreen) {
      return (
        <AppLoading
          startAsync={this._loadResourcesAsync}
          onError={this._handleLoadingError}
          onFinish={this._handleFinishLoading}
        />
      );
    } else {
      return (
        <View style={styles.container}>
          {Platform.OS === "ios" && <StatusBar barStyle="default" />}
          {/* <AppNavigator /> */}

          {/* <MainSwitchNavigator /> */}
          <MainStackNavigator />
        </View>
      );
    }
  }

  _loadResourcesAsync = async () => {
    return Promise.all([]);
  };

  _handleLoadingError = (error) => {
    console.npmarn(error);
  };

  _handleFinishLoading = () => {
    this.setState({ isLoadingComplete: true });
  };
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
  },
});
