<template>
  <v-footer dark padless>
    <v-card class="flex" flat tile>
      <v-card-title style="padding-bottom: 0px">
        <div class="subheading">Get connected with us on social networks!</div>

        <v-spacer></v-spacer>

        <v-btn v-for="icon in icons" :key="icon" class="mx-4" dark icon>
          <a
            :href="icon.url"
            :target="icon.target"
            style="text-decoration: none; color: inherit"
          >
            <v-icon size="24px">{{ icon.name }}</v-icon>
          </a>
        </v-btn>
      </v-card-title>

      <v-card-text class="white--text text-center">
        <v-layout style="padding-bottom: 10px; font-size: 18px;" row justify-center align-center
          ><v-icon>mdi-emoticon-dead-outline</v-icon>&nbsp;BlueScreens&nbsp;
          <v-icon>mdi-emoticon-dead-outline</v-icon></v-layout
        >
        {{ new Date().getFullYear() }} — <strong>Java Academy</strong>
      </v-card-text>
    </v-card>
  </v-footer>
</template>

<script>
export default {
  data: () => ({
    icons: [
      {
        name: "mdi-facebook",
        url: "https://www.facebook.com/IBM/",
        target: "_blank",
      },
      {
        name: "mdi-twitter",
        url: "https://twitter.com/IBM/",
        target: "_blank",
      },
      {
        name: "mdi-linkedin",
        url: "https://www.linkedin.com/company/ibm/",
        target: "_blank",
      },
      {
        name: "mdi-instagram",
        url: "https://www.instagram.com/ibm/",
        target: "_blank",
      },
    ],
  }),
};
</script>