import * as React from "react";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";

import SignUpScreen from "../screens/SignUpScreen";
import LogInScreen from "../screens/LogInScreen";
import RestorePasswordScreen from "../screens/RestorePasswordScreen";
import DrawerNavigator from "./DrawerNavigator";

const Stack = createStackNavigator();

function MainStackNavigator() {
  console.disableYellowBox = true;
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="LogIn">
        <Stack.Screen
          name="LogIn"
          component={LogInScreen}
          options={{ headerShown: false }}
        />
        <Stack.Screen
          name="SignUp"
          component={SignUpScreen}
          options={{ headerShown: false }}
        />
        <Stack.Screen
          name="RestorePassword"
          component={RestorePasswordScreen}
          options={{ headerShown: false }}
          initialParams={{ user: null, username: "" }}
        />
        <Stack.Screen
          name="Drawer"
          component={DrawerNavigator}
          options={{ headerShown: false }}
          initialParams={{ user: null }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default MainStackNavigator;
