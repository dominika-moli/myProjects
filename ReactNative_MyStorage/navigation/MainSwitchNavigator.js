import { createSwitchNavigator, createAppContainer } from "react-navigation";
import SignUpScreen from "../screens/SignUpScreen";
import LogInScreen from "../screens/LogInScreen";
import RestorePasswordScreen from "../screens/RestorePasswordScreen";
import DrawerNavigator from "./DrawerNavigator";
import { createDrawerNavigator } from "@react-navigation/drawer";

import HomeScreen from "../screens/HomeScreen";
import BarCodeScreen from "../screens/BarCodeScreen";
import ShoppingListScreen from "../screens/ShoppingListScreen";
import LogoutScreen from "../screens/LogoutScreen";

export default createAppContainer(
  createSwitchNavigator({
    Drawer: DrawerNavigator,
    LogIn: LogInScreen,
    SignUp: SignUpScreen,
    RestorePassword: RestorePasswordScreen,
  })
);
