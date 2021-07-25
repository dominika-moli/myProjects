import Vue from "vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";
import colors from 'vuetify/lib/util/colors'


Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
          light: {
            // primary: colors.blue.lighten2,
            // accent: colors.grey.lighten3,
            // secondary: colors.amber.lighten3,
            // info: colors.teal.lighten1,
            // warning: colors.amber.base,
            // error: colors.deepOrange.accent4,
            // success: colors.green.accent3
          },
          dark: {
            primary: colors.blue.darken2,
            accent: colors.grey.darken3,
            secondary: colors.amber.darken3,
            info: colors.teal.lighten1,
            warning: colors.amber.base,
            error: colors.deepOrange.accent4,
            success: colors.green.accent3
          }
        }
    }       
});
