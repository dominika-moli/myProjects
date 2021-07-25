import Vue from "vue";
import Vuex from "vuex";
import router from "../router/index";
import axios from "axios";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    email: "",
    password: "",
    loggedIn: false,
    deleteError: false,
    certifications: [],
    user: {},
    userActiveVouchers: [],
    userPendingVouchers: [],
    loginInfo: "Please Enter your credentials.",
    loginColor: "",
    vouchers: [],
    users: [],
    pendingVouchers: [],
    proposedCertifications: [],
    skills: []
  },
  mutations: {
    emailMutation(state, value) {
      state.email = value;
    },
    passwordMutation(state, value) {
      state.password = value;
    },
    loggedInMutation(state, value) {
      console.log("loggedInMutation: " + value);
      if (value === null) {
        state.loggedIn = false;
      } else {
        state.loggedIn = value;
      }
    },
    certificationsMutation(state, value) {
      state.certifications = value;
    },
    userMutation(state, value) {
      state.user = value;
    },
    userActiveVouchersMutation(state, value) {
      state.user = value;
    },
    userPendingVouchersMutation(state, value) {
      state.user = value;
    },
    loginInfoMutation(state, value) {
      state.loginInfo = value;
    },
    loginColorMutation(state, value) {
      state.loginColor = value;
    },
    deleteErrorMutation(state, value) {
      state.deleteError = value;
    },
    pendingVouchersMutation(state, value) {
      state.pendingVouchers = value;
    },
    usersMutation(state, value) {
      state.users = value;
    },
    vouchersMutation(state, value) {
      state.vouchers = value;
    },
    proposedCertificationsMutation(state, value) {
      state.proposedCertifications = value;
    },
    skillsMutation(state, value) {
      state.skills = value;
    }
  },
  getters: {
    email(state) {
      return state.email;
    },
    password(state) {
      return state.password;
    },
    certifications(state) {
      return state.certifications;
    },
    user(state) {
      return state.user;
    },
    userActiveVouchers(state) {
      return state.userActiveVouchers;
    },
    userPendingVouchers(state) {
      return state.userPendingVouchers;
    },
    loggedIn(state) {
      return state.loggedIn;
    },
    loginInfo(state) {
      return state.loginInfo;
    },
    loginColor(state) {
      return state.loginColor;
    },
    vouchers(state) {
      return state.vouchers;
    },
    users(state) {
      return state.users;
    },
    deleteError(state) {
      return state.deleteError;
    },
    pendingVouchers(state) {
      return state.pendingVouchers;
    },
    proposedCertifications(state) {
      return state.proposedCertifications;
    },
    skills(state) {
      return state.skills;
    }
  },
  actions: {
    async loginToApp({ commit, rootState }) {
      const url =
        "http://localhost:8080/sign-in?email=" +
        rootState.email +
        "&password=" +
        rootState.password;

      try {
        const { data } = await axios.post(url);
        console.log(data);
        commit("userMutation", data.user);
        commit("loggedInMutation", true);

        localStorage.setItem("token", data.confirmationToken);
        localStorage.setItem("userId", data.user.id);
        localStorage.setItem("userRoleId", data.user.role.id);

        router.push("/");
      } catch (err) {
        console.log(err);
        rootState.email = "";
        rootState.password = "";
        rootState.loginInfo = "Login not successful. Try again!";
        rootState.loginColor = "red";
      }
    },
    async createCertificationRequest(
      { commit, rootState },
      certificationRequest
    ) {
      const url = "http://localhost:8080/certifications/";
      const headers = {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token")
      };
      try {
        const { data } = await axios.post(url, certificationRequest, {
          headers
        });
        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },
    async createDeleteSkillsRequest(
      { commit, rootState },
      deleteSkillsRequest
    ) {
      try {
        const { data } = await axios.delete(
          "http://localhost:8080/certs/deleteSkills/" + deleteSkillsRequest.id,
          deleteSkillsRequest
        );
        console.log(data);
      } catch (e) {
        console.log(e);
      }
    },
    async createSkillToCertRequest({ commit, rootState }, skillToCertRequest) {
      console.log("request add skill to cert");
      console.log(skillToCertRequest.id);
      console.log(skillToCertRequest.skillId);
      try {
        const { data } = await axios.put(
          "http://localhost:8080/certs/addSkill/" + skillToCertRequest.id,
          skillToCertRequest,
          {
            params: {
              skillId: skillToCertRequest.skillId
            }
          }
        );
        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },
    //http://localhost:8080/certs/addSkill/1000?skillId=20001
    async createVoucherRequest({ commit, rootState }, voucherRequest) {
      const url = "http://localhost:8080/vouchers/add";
      const headers = {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("token")
      };
      try {
        const { data } = await axios.post(url, voucherRequest, {
          headers,
          params: {
            userId: voucherRequest.userId,
            certId: voucherRequest.certId
          }
        });
        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },
    async createAcceptVoucherRequest(
      { commit, rootState },
      acceptVoucherRequest
    ) {
      const url = "http://localhost:8080/vouchers/activate";
      try {
        const { data } = await axios.put(url, acceptVoucherRequest, {
          params: {
            id: acceptVoucherRequest.id
          }
        });

        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },

    async deleteVoucherRequest({ commit, rootState }, deleteVoucherRequest) {
      const url = "http://localhost:8080/vouchers/delete";
      try {
        await axios.delete(url, {
          params: {
            id: deleteVoucherRequest.id
          }
        });
      } catch (err) {
        console.log(err);
      }
    },
    async deactivateVoucherRequest(
      { commit, rootState },
      deactivateVoucherRequest
    ) {
      const url = "http://localhost:8080/vouchers/deactivate";
      try {
        const { data } = await axios.post(url, deactivateVoucherRequest, {
          params: {
            id: deactivateVoucherRequest.id
          }
        });
        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },

    async createChangeRoleRequest({ commit, rootState }, changeRoleRequest) {
      try {
        const { data } = await axios.put(
          "http://localhost:8080/changeRole/" + changeRoleRequest.id,
          changeRoleRequest,
          {
            params: {
              role: changeRoleRequest.role.charAt(0)
            }
          }
        );

        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },
    async createDeleteCertificationRequest(
      { commit, rootState },
      deleteCertificationRequest
    ) {
      const url =
        "http://localhost:8080/certifications/" + deleteCertificationRequest.id;
      try {
        const { data } = await axios.delete(url, deleteCertificationRequest);
        commit("deleteErrorMutation", false);
        console.log(data);
      } catch (err) {
        commit("deleteErrorMutation", true);
        console.log(err);
      }
    },
    async createEditCertification(
      { commit, rootState },
      editCertificationRequest
    ) {
      const url = "http://localhost:8080/certs/edit";
      try {
        const { data } = await axios.put(url, editCertificationRequest, {
          params: {
            id: editCertificationRequest.id,
            name: editCertificationRequest.name,
            url: editCertificationRequest.url,
            price: editCertificationRequest.price,
            currency: editCertificationRequest.currency,
            state: editCertificationRequest.state
          }
        });
        console.log(data);
      } catch (err) {
        console.log(err);
      }
    },
    async createCancelVoucherRequest(
      { commit, rootState },
      cancelVoucherRequest
    ) {
      try {
        const { data } = await axios.delete(
          "http://localhost:8080/vouchers/" + cancelVoucherRequest.id,
          cancelVoucherRequest
        );
        console.log(data);
      } catch (e) {
        console.log(e);
      }
    }
  }
});
