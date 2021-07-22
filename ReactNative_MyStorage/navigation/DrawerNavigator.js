import * as React from "react";
import { createDrawerNavigator } from "@react-navigation/drawer";
import Icons from "react-native-vector-icons/FontAwesome";

import HomeScreen from "../screens/HomeScreen";
import BarCodeScreen from "../screens/BarCodeScreen";
import ShoppingListScreen from "../screens/ShoppingListScreen";
import LogoutScreen from "../screens/LogoutScreen";
import MinusBarCode from "../screens/MinusBarCode";
import MaterialCommunityIcons from "react-native-vector-icons/MaterialCommunityIcons";
import NearestShop from "../screens/NearestShop";
import ImportScreen from "../screens/ImportScreen";

import { useTranslation } from "react-i18next";
import "../i18n";

const Drawer = createDrawerNavigator();

function DrawerNavigator(props) {
  const { t, i18n } = useTranslation();
  return (
    <Drawer.Navigator
      initialRouteName="Home"
      drawerContentOptions={{
        activeBackgroundColor: "#119da4",
        activeTintColor: "#E0E1DD",
      }}
      drawerStyle={{
        paddingTop: 5,
      }}
    >
      <Drawer.Screen
        name={t("Home")}
        initialParams={{ params: props.route }}
        component={HomeScreen}
        options={{
          headerShown: true,
          drawerIcon: ({ focused }) => (
            <Icons name="home" color={focused ? "#E0E1DD" : "#ccc"} size={23} />
          ),
        }}
      />
      <Drawer.Screen
        name={t("Shoppinglist")}
        component={ShoppingListScreen}
        options={{
          headerShown: true,
          drawerIcon: ({ focused }) => (
            <Icons
              name="list-ul"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />

      <Drawer.Screen
        name={t("Import Shoppinglist")}
        component={ImportScreen}
        options={{
          headerShown: true,
          drawerIcon: ({ focused }) => (
            <MaterialCommunityIcons
              name="home-import-outline"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />

      <Drawer.Screen
        name="Barcode"
        component={BarCodeScreen}
        options={{
          title: t("New Groceries"),
          headerShown: true,
          headerTintColor: "#000",
          headerStyle: { backgroundColor: "#fff" },
          headerTitleStyle: { color: "#000" },
          drawerIcon: ({ focused }) => (
            <Icons
              name="shopping-cart"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />
      <Drawer.Screen
        name="Consume Groceries"
        component={MinusBarCode}
        options={{
          title: t("Consume Groceries"),
          headerShown: true,
          headerTintColor: "#000",
          headerStyle: { backgroundColor: "#fff" },
          headerTitleStyle: { color: "#000" },
          drawerIcon: ({ focused }) => (
            <MaterialCommunityIcons
              name="food"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />

      <Drawer.Screen
        name="Map"
        component={NearestShop}
        options={{
          title: t("Nearest Shop"),
          headerShown: true,
          headerTintColor: "#000",
          headerStyle: { backgroundColor: "#fff" },
          headerTitleStyle: { color: "#000" },
          drawerIcon: ({ focused }) => (
            <MaterialCommunityIcons
              name="map-marker-radius"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />
      <Drawer.Screen
        name={t("Logout")}
        component={LogoutScreen}
        options={{
          drawerIcon: ({ focused }) => (
            <Icons
              name="sign-out"
              color={focused ? "#E0E1DD" : "#ccc"}
              size={23}
            />
          ),
        }}
      />
    </Drawer.Navigator>
  );
}

export default DrawerNavigator;
