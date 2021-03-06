import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import {locales} from './locales';
import { NativeModules, Platform } from 'react-native';

const deviceLanguage =
   Platform.OS === 'ios'
      ? NativeModules.SettingsManager.settings.AppleLocale ||
      NativeModules.SettingsManager.settings.AppleLanguages[0] // iOS 13
      : NativeModules.I18nManager.localeIdentifier;

console.log(deviceLanguage); //en_US

i18n
  .use(initReactI18next) // passes i18n down to react-i18next
  .init({
    resources: locales,
    // lng: "sk",
    fallbackLng: 'en',
    lng: deviceLanguage.substring(0,2),

    keySeparator: false, // we do not use keys in form messages.welcome

    interpolation: {
      escapeValue: false, // react already safes from xss
    },
  });

export default i18n;
