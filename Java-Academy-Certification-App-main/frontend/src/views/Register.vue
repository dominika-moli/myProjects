<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <v-flex md4 sm8 xs12>
        <v-card class="elevation-12">
          <v-toolbar dark color="black">
            <v-toolbar-title
              >Welcome to Certifications app - Register</v-toolbar-title
            >
          </v-toolbar>
          <v-card-text>
            <p :style="{ color: regError }">
              {{ registerInfo }}
            </p>
            <v-form ref="form" v-model="validRegForm">
              <v-row>
                <v-text-field
                  class="mx-4"
                  v-model="firstname"
                  label="First Name"
                  :rules="nameRules"
                  type="text"
                  :counter="20"
                >
                </v-text-field>
                <v-text-field
                  class="mx-4"
                  v-model="lastname"
                  :rules="nameRules"
                  label="Last Name"
                  type="text"
                  :counter="20"
                >
                </v-text-field>
              </v-row>

              <v-text-field
                label="Email"
                type="text"
                class="my-2"
                v-model="regEmail"
                prepend-icon="mdi-email"
                :rules="emailRules"
              >
              </v-text-field>

              <v-row>
                <v-text-field
                  class="mx-4"
                  label="Password"
                  v-model="regPassword"
                  :rules="minRules"
                  :append-icon="value ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="() => (value = !value)"
                  :type="value ? 'password' : 'text'"
                >
                </v-text-field>
                <v-text-field
                  class="mx-4"
                  v-model="regPasswordConfirm"
                  label="Confirm Password"
                  :rules="[minRules, passwordConfirmationRule]"
                  :append-icon="valueCheck ? 'mdi-eye' : 'mdi-eye-off'"
                  @click:append="() => (valueCheck = !valueCheck)"
                  :type="valueCheck ? 'password' : 'text'"
                >
                </v-text-field>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions class="justify-center">
            <v-btn
              color="primary"
              width="100"
              @click="registerToApp"
              :disabled="!validRegForm"
              >Register</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from "vuex";
import axios from "axios";

export default {
  name: "Register",
  data() {
    return {
      validRegForm: true,
      registerInfo: "Please Enter your credentials.",
      regError: "",
      value: String,
      valueCheck: String,
      firstname: "",
      lastname: "",
      regEmail: "",
      regPassword: "",
      regPasswordConfirm: "",
      emailRules: [
        (value) => !!value || "Email is required",
        (value) => /.+@.+/.test(value) || "Email must be valid",
      ],
      minRules: [
        (v) => !!v || "Password is required",
        (v) => v.length >= 8 || "Password must be more than 8 characters",
      ],
      nameRules: [
        (v) => !!v || "Name is required",
        (v) => v.length >= 3 || "Name must be more than 3 characters",
        (v) => (v && v.length <= 15) || "Name must be less than 15 characters",
      ],
    };
  },

  computed: {
    passwordConfirmationRule() {
      return (
        !!this.regPasswordConfirm || "Password is required",
        this.regPasswordConfirm.length >= 8 ||
          "Password must be more than 8 characters",
        this.regPassword === this.regPasswordConfirm || "Password must match"
      );
    },
  },
  methods: {
    ...mapActions(["loginToApp"]),
    async registerToApp() {
      const url = "http://localhost:8080/sign-up";
      const body = {
        name: this.firstname,
        surname: this.lastname,
        email: this.regEmail,
        password: this.regPassword,
        role: { id: "S" },
      };
      try {
        const { data } = await axios.post(url, body);
        console.log(data);

        this.$store.commit("emailMutation", this.regEmail);
        this.$store.commit("passwordMutation", this.regPassword);
        this.loginToApp();
      } catch (err) {
        this.registerInfo = "Email already taken. Try again!";
        this.regError = "red";
        this.regEmail = "";
        console.log(err);
      }
    },
  },
};
</script>
