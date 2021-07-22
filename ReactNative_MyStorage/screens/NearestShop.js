import React, { useState, useEffect } from "react";
import { Platform, Text, View, StyleSheet, Dimensions } from "react-native";
import * as Location from "expo-location";
import MapView from "react-native-maps";
import { Marker } from "react-native-maps";

export default class NearestShop extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      latitude: null,
      longitude: null,
      location: null,
      setErrorMsg: "",
      markers: []
    };
  }

  componentDidMount() {
    this.lokacia();
    const { navigation } = this.props;

    this.focusListener = navigation.addListener("focus", () => {
      this.lokacia();
    });
  }

  componentWillUnmount() {
    if (this.focusListener != null && this.focusListener.remove)
      this.focusListener.remove();
  }

  lokacia = async () => {
    let { status } = await Location.requestPermissionsAsync();
    if (status !== "granted") {
      setErrorMsg("Permission to access location was denied");
      return;
    }
    let locationPom = await Location.getCurrentPositionAsync({});
    this.setState({ location: locationPom });
    this.setState({ latitude: locationPom.coords.latitude });
    this.setState({ longitude: locationPom.coords.longitude });
    console.log(this.state.location);
    console.log(this.state.latitude);
    console.log(this.state.longitude);
    console.log("tuc tuc");

    setTimeout(() => this.getNearestShops(), 3000);
    // this.getNearestShops();
  };

  getNearestShops() {
    const url =
      "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=supermarket&inputtype=textquery&fields=geometry,formatted_address,name,opening_hours,rating&locationbias=circle:5000@" +
      this.state.latitude +
      "," +
      this.state.longitude +
      "&key=AIzaSyCzu0JULvwRXR2n7ijNuGDPSc5Ea5pSJIo";
    fetch(url)
      .then((response) => response.json())
      .then((JsonResponse) => {
        console.log(JsonResponse);
        let toMakrkers = JsonResponse.candidates;
        let newMarkers = [];
        toMakrkers.map((element, index) => {
          const obj = {};
          obj.id = index;
          obj.name = element.name;
          obj.rating = element.rating;
          obj.marker = {
            latitude: element.geometry.location.lat,
            longitude: element.geometry.location.lng,
          };

          newMarkers.push(obj);
        });
        this.setState({markers: newMarkers});
      })
      .catch((error) => {
        alert(error);
      });
  }

  render() {
    return (
      <View style={styles.container}>
        <MapView
          style={styles.map}
          region={{
            latitude: this.state.latitude,
            longitude: this.state.longitude,
            latitudeDelta: 0.0922,
            longitudeDelta: 0.0421,
          }}
          showsCompass={false}
          loadingEnabled={true}
          showsUserLocation={true}
        >
        {this.state.markers.map((marker, index) => (
          <Marker
            key={index}
            coordinate={marker.marker}
            title={marker.name}
          />
        ))}
        </MapView>
      </View>
    );
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
  map: {
    width: Dimensions.get("window").width,
    height: Dimensions.get("window").height,
  },
});
