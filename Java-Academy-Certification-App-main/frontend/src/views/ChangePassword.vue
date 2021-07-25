<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <v-flex md4 sm8 xs12>
        <v-card class="elevation-12">
          <v-toolbar dark color="black">
            <v-toolbar-title
              >Welcome to Certifications app - Change Password</v-toolbar-title
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
              width="180"
              @click="changePassword"
              :disabled="!validRegForm"
              >Change Password</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
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
      regPassword: "",
      regPasswordConfirm: "",
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
    async changePassword() {
      const url =
        "http://localhost:8080/changepassword?token=" +
        this.$route.params.token +
        "&password=" +
        this.regPassword;

      try {
        const { data } = await axios.post(url);
        console.log(data);
        this.registerInfo = "Password changed! Login again with new password.";
        this.regError = "green";
        setTimeout(() => this.$router.push("/login"), 1500);
      } catch (err) {
        this.registerInfo = "Reset token not found. Password not changed!";
        this.regError = "red";
        console.log(err);
      }
    },
  },
};
</script>
