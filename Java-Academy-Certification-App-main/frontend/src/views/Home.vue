<template>
  <div class="home">
    <!--Student's home page-->
    <div class="student" v-if="localStorage.getItem('userRoleId') === 'S'">
      <!--Student's active vouchers-->
      <v-row>
        <v-col cols="12">
          <h1 style="margin-left: 2rem">My Active Vouchers</h1>
          <div
            v-if="myActiveVouchers !== undefined && myActiveVouchers.length > 0"
            class="card-wrapper"
          >
            <v-row>
              <router-link
                v-for="voucher in myActiveVouchers"
                :key="voucher.id"
                :to="'/voucherDetail/' + voucher.id"
                v-bind:id="'certDashboardReceivedVoucher' + voucher.id"
                tag="div"
              >
                <v-col>
                  <VoucherItem
                    style="cursor: pointer"
                    :key="voucher.id"
                    :voucher="voucher"
                  />
                </v-col>
              </router-link>
            </v-row>
          </div>
          <div v-else>
            <h3
              v-if="
                myPendingVouchers === undefined || myPendingVouchers.length <= 0
              "
            >
              You have no active vouchers. Take a look at available
              certifications.
            </h3>
            <h3 v-else>
              You have no active vouchers. Take a look at available
              certifications or wait until your pending vouchers get active.
            </h3>
          </div>
        </v-col>
      </v-row>
      <!--Student's pending vouchers waiting to be accepted-->
      <v-row>
        <v-col cols="12">
          <h1 style="margin-left: 2rem">My Pending Vouchers</h1>
          <div
            v-if="
              myPendingVouchers !== undefined && myPendingVouchers.length > 0
            "
            class="card-wrapper"
          >
            <v-row>
              <router-link
                v-for="voucher in myPendingVouchers"
                :key="voucher.id"
                :to="'/voucherDetail/' + voucher.id"
                v-bind:id="'certDashboardReceivedVoucher' + voucher.id"
                tag="div"
              >
                <v-col>
                  <VoucherItem
                    style="cursor: pointer"
                    :key="voucher.id"
                    :voucher="voucher"
                  />
                </v-col>
              </router-link>
            </v-row>
          </div>
          <div v-else>
            <h3>You have no pending vouchers.</h3>
          </div>
        </v-col>
      </v-row>
    </div>
    <!--Manager's home page-->
    <div
      class="manager"
      v-if="
        localStorage.getItem('userRoleId') === 'M' ||
        localStorage.getItem('userRoleId') === 'A'
      "
    >
      <!--Pending vouchers that need to be activated by manager-->
      <v-row>
        <v-col cols="12">
          <h1>Pending Vouchers Waiting For Approval</h1>
          <div
            v-if="
              allPendingVouchers !== undefined && allPendingVouchers.length > 0
            "
          >
            <v-btn
              @click.stop="activateAllState = true"
              color="primary"
              style="margin: 15px"
              >Activate All</v-btn
            >
            <v-dialog persistent width="600" v-model="activateAllState">
              <v-card class="mx-auto">
                <v-card-title>
                  Do you really want to activate all pending vouchers?
                </v-card-title>
                <v-card-actions>
                  <div v-if="!allActivated">
                    <v-btn
                      style="margin-right: 5px"
                      @click="activateAll"
                      color="primary"
                    >
                      Yes, Activate All
                    </v-btn>
                    <v-btn
                      color="secondary"
                      @click.stop="activateAllState = false"
                    >
                      No
                    </v-btn>
                  </div>
                  <v-alert v-else type="success"
                    >All pending vouchers activated.</v-alert
                  >
                </v-card-actions>
              </v-card>
            </v-dialog>
            <v-row>
              <router-link
                v-for="pendingVoucher in allPendingVouchers"
                :key="pendingVoucher.id"
                :to="'/voucherDetail/' + pendingVoucher.id"
                tag="div"
              >
                <v-col>
                  <VoucherItem
                    style="cursor: pointer"
                    :voucher="pendingVoucher"
                  />
                </v-col>
              </router-link>
            </v-row>
          </div>
          <div v-else>
            <h3>No pending vouchers to activate.</h3>
          </div>
        </v-col>
      </v-row>
    </div>
    <!--Everyone's home page-->
    <div class="all">
      <!--All available certifications that are in the database-->
      <v-row>
        <v-col cols="12">
          <div v-if="certifications !== undefined && certifications.length > 0">
            <h1>Available Certifications</h1>
            <v-container fluid>
              <v-row align="center" justify="center">
                <v-col class="d-flex" cols="12" sm="3">
                  <v-select
                    :items="skillNames"
                    label="Show by category"
                    solo
                    :menu-props="{ offsetY: true }"
                    v-model="searchSkill"
                  ></v-select>
                </v-col>
                <v-col class="d-flex" cols="12" sm="3">
                  <v-text-field
                    placeholder="Search for available certifications"
                    prepend-inner-icon="mdi-magnify"
                    class="expanding-search mt-1"
                    v-model="search"
                    filled
                    dense
                  >
                  </v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <v-row>
              <router-link
                v-for="certification in filteredCertifications"
                :key="certification.id"
                :to="'/certificationDetail/' + certification.id"
                tag="div"
              >
                <v-col>
                  <CertificationItem
                    style="cursor: pointer"
                    :certification="certification"
                  ></CertificationItem>
                </v-col>
              </router-link>
            </v-row>
          </div>
          <div v-else>
            <h3>No Certifications found</h3>
          </div>
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import VoucherItem from "@/components/VoucherItem.vue";
import CertificationItem from "@/components/CertificationItem.vue";
import { mapGetters, mapMutations } from "vuex";
import axios from "axios";

export default Vue.extend({
  name: "Home",
  components: {
    VoucherItem,
    CertificationItem,
  },
  data() {
    return {
      localStorage,
      myActiveVouchers: [],
      myPendingVouchers: [],
      //certifications: [],
      allPendingVouchers: [],
      activateAllState: false,
      allActivated: false,
      certificationNames: [],
      skillNames: [],
      skills: [],
      skillIds: [],
      skillCertifications: {},
      search: "",
      searchSkill: "",
      skillURL: "",
    };
  },
  computed: {
    ...mapGetters(["vouchers", "pendingVouchers", "skills"]),
    certifications: {
      get() {
        return this.$store.state.certifications;
      },
      set(value) {
        this.$store.commit("certificationsMutation", value);
      },
    },
    // for searching certifications
    filteredCertifications() {
      // vyfiltruje certifikaty podla mena
      if (this.searchSkill == "" || this.searchSkill == "All")
        return this.certifications.filter((cert) => {
          return cert.name.toLowerCase().includes(this.search.toLowerCase());
        });
      // vyfiltruje certifikaty podla kategorie (skill), zatial nefunguje :(
      else {
        const skills = this.skills.filter((skill) => {
          return skill.name.toLowerCase() === this.searchSkill.toLowerCase();
        });
        return this.skillCertifications[skills[0].id];
      }
    },
  },
  async mounted() {
    const { data } = await axios.get(
      "http://localhost:8080/users/" +
        localStorage.getItem("userId") +
        "/vouchers"
    );
    this.myActiveVouchers = data._embedded.vouchers.filter(
      (v) => v.state === "ACTIVE"
    );
    this.myPendingVouchers = data._embedded.vouchers.filter(
      (v) => v.state === "PENDING"
    );
    await axios
      .get("http://localhost:8080/certifications")
      .then(
        (response) =>
          (this.certifications = response.data._embedded.certifications)
      );
    await axios.get("http://localhost:8080/vouchers").then((response) => {
      this.allPendingVouchers = response.data._embedded.vouchers.filter(
        (v) => v.state === "PENDING"
      );
    });

    await axios.get("http://localhost:8080/certifications").then((response) => {
      console.log("Certifications:");
      console.log(response.data._embedded.certifications);
      const certs = response.data._embedded.certifications;
      this.changeRating(certs);

      this.certificationsMutation(response.data._embedded.certifications);
      this.certificationNames = response.data._embedded.certifications.map(
        (cert) => cert.name
      );
    });
    await axios.get("http://localhost:8080/skills").then((response) => {
      console.log("Skills:");
      console.log(response.data._embedded.skills);
      this.skillsMutation(response.data._embedded.skills);
      this.skills = response.data._embedded.skills;
      this.skillNames = response.data._embedded.skills.map(
        (skill) => skill.name
      );
      console.log("Skill names: " + this.skillNames);
      this.skillNames.push("All");
      this.skillIds = response.data._embedded.skills.map((skill) => skill.id);
    });
    await this.skillIds.forEach((id) => {
      axios
        .get("http://localhost:8080/skills/" + id + "/certifications")
        .then((response) => {
          this.skillCertifications[id] = response.data._embedded.certifications;
          console.log("All skill certs");
          console.log(this.skillCertifications);
        });
    });
    await axios.get("http://localhost:8080/vouchers").then((response) => {
      const pendingVouchers = response.data._embedded.vouchers.filter(
        (voucher) => voucher.state === "PROPOSED"
      );
      console.log("Pending vouchers get request and filter");
      console.log(pendingVouchers);
      this.pendingVouchersMutation(pendingVouchers);
    });
    await axios.get("http://localhost:8080/certifications").then((response) => {
      const proposedCertifications =
        response.data._embedded.certifications.filter(
          (certification) => certification.state === "PROPOSED"
        );
      console.log("Pending vouchers get request and filter");
      console.log(proposedCertifications);
      this.proposedCertificationsMutation(proposedCertifications);
    });
  },
  methods: {
    activateAll() {
      this.allActivated = true;
      for (let i = 0; i < this.allPendingVouchers.length; i++) {
        this.$store.dispatch("createAcceptVoucherRequest", {
          id: this.allPendingVouchers[i].id,
        });
      }
      setTimeout(() => {
        this.activateAllState = false;
        window.location.reload();
      }, 1000);
    },
    async changeRating(certs) {
      await Promise.all(
        certs.map(async (cert, index) => {
          const data = await axios.get(
            "http://localhost:8080/certReview/" + cert.id
          );
          console.log(data.data);
          Vue.set(certs[index], "rating", data.data);
        })
      );
      this.certificationsMutation(certs);
    },
    ...mapMutations([
      "vouchersMutation",
      "certificationsMutation",
      "pendingVouchersMutation",
      "proposedCertificationsMutation",
      "skillsMutation",
    ]),
    skillSelected() {
      this.search = "";
    },
  },
});
</script>
