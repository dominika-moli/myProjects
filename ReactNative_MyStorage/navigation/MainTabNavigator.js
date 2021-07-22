import { createStackNavigator } from "react-navigation-stack";
import { createBottomTabNavigator } from "react-navigation-tabs";

import HomeScreen from "../screens/HomeScreen";
import SignUpScreen from "../screens/SignUpScreen";
import LogInScreen from "../screens/LogInScreen";
import RestorePasswordScreen from "../screens/RestorePasswordScreen";
import BarCodeScreen from "../screens/BarCodeScreen";
import ShoppingListScreen from "../screens/ShoppingListScreen";
import LogoutScreen from "../screens/LogoutScreen";

const HomeStack = createStackNavigator({
  Home: HomeScreen,
});

const SignUpStack = createStackNavigator({
  SignUp: SignUpScreen,
});

const LogInStack = createStackNavigator({
  LogIn: LogInScreen,
});

const RestorePasswordStack = createStackNavigator({
  RestorePassword: RestorePasswordScreen,
});

const BarCodeStack = createStackNavigator({
  BarCode: BarCodeScreen,
});

const ShoppingListStack = createStackNavigator({
  ShoppingList: ShoppingListScreen,
});

const LogoutStack = createStackNavigator({
  LogOut: LogoutScreen,
});



HomeStack.navigationOptions = {
  tabBarLabel: "Home",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

SignUpStack.navigationOptions = {
  tabBarLabel: "Sign Up",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

LogInStack.navigationOptions = {
  tabBarLabel: "Log In",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

RestorePasswordStack.navigationOptions = {
  tabBarLabel: "Reset Password",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

BarCodeStack.navigationOptions = {
  tabBarLabel: "Scan",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

ShoppingListStack.navigationOptions = {
  tabBarLabel: "Shopping List",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

LogoutStack.navigationOptions = {
  tabBarLabel: "Log Out",
  tabBarVisible: false,
  tabBarOptions: { showLabel: false },
};

export default createBottomTabNavigator({
  LogInStack,
  HomeStack,
  SignUpStack,
  RestorePasswordStack,
  BarCodeStack,
  ShoppingListStack,
  LogoutStack
});
