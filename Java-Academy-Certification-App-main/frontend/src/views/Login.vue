<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <v-flex md4 sm8 xs12>
        <v-card class="elevation-12">
          <v-toolbar dark color="black">
            <v-toolbar-title>Welcome to Certifications app</v-toolbar-title>
          </v-toolbar>
          <v-card-text>
            <p :style="{ color: loginColor }">
              {{ loginInfo }}
            </p>
            <v-form ref="form" v-model="validForm">
              <v-text-field
                prepend-icon="mdi-account"
                label="Login"
                type="text"
                :counter="30"
                :rules="emailRules"
                v-model="email"
              >
              </v-text-field>
              <v-text-field
                prepend-icon="mdi-lock"
                label="Password"
                :append-icon="value ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="() => (value = !value)"
                :type="value ? 'password' : 'text'"
                :rules="minRules"
                v-model="password"
              >
              </v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions class="justify-center">
            <v-btn color="secondary" width="100" @click="register"
              >Register</v-btn
            >
            <v-btn
              :disabled="!validForm"
              color="primary"
              @click="loginToApp"
              width="100"
              >Login</v-btn
            >
          </v-card-actions>

          <p
            class="styleForgot"
            :style="{ color: '#1976d2', cursor: 'pointer' }"
            width="100"
            @click="forgot"
          >
            Forgot your password?
          </p>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from "vuex";
import { mapGetters } from "vuex";
import router from "../router/index";

export default {
  name: "Login",
  data() {
    return {
      validForm: true,
      value: String,
      emailRules: [
        (value) => !!value || "Email is required",
        (value) => /.+@.+/.test(value) || "Email must be valid",
      ],
      minRules: [(value) => value.length >= 8 || "Min 8 characters"],
    };
  },
  computed: {
    email: {
      get() {
        return this.$store.getters.email;
      },
      set(value) {
        this.$store.commit("emailMutation", value);
      },
    },
    password: {
      get() {
        return this.$store.getters.password;
      },
      set(value) {
        this.$store.commit("passwordMutation", value);
      },
    },
    loginInfo: {
      get() {
        return this.$store.getters.loginInfo;
      },
      set(value) {
        this.$store.commit("loginInfoMutation", value);
      },
    },

    loginColor: {
      get() {
        return this.$store.getters.loginColor;
      },
      set(value) {
        this.$store.commit("loginColorMutation", value);
      },
    },
  },

  methods: {
    ...mapActions(["loginToApp"]),

    register() {
      router.push("/register");
    },

    forgot() {
      router.push("/resetPassword");
    },
  },
};
</script>

<style scoped>
.styleForgot:hover {
  text-decoration: underline;
}
</style>