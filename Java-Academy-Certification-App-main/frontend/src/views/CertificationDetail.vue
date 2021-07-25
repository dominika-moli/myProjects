<template>
  <div class="certification">
    <div class="student" v-if="localStorage.getItem('userRoleId') === 'S'">
      <v-card class="mx-auto" width="600">
        <v-card-title>{{ currentCertification.name }}</v-card-title>
        <v-card-text class="text--primary">
          <div>
            URL:
            <a :href="currentCertification.url" target="_blank">{{
              currentCertification.url
            }}</a>
          </div>
          <div
            v-if="
              currentCertification.price == null ||
              currentCertification.price === 0
            "
          >
            Free
          </div>
          <div v-else>
            Price:
            {{ currentCertification.price }}
            {{ currentCertification.currency }}
          </div>
          <div v-for="skill in skills" :key="skill.id">
            <div>Skill: {{ skill.name }}</div>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-dialog
            v-model="newCertificationDialog"
            transition="dialog-bottom-transition"
            max-width="600"
            persistent
          >
            <template v-slot:activator="{ on }">
              <v-btn
                @click="newCertificationDialog = true"
                v-on="on"
                :disabled="studentOwns"
                color="primary"
                >Request Voucher</v-btn
              >
            </template>
            <template v-slot:default="dialog">
              <v-card>
                <v-toolbar color="primary" dark
                  >Hey, {{ user.name }}!</v-toolbar
                >
                <v-card-text>
                  <div class="text-h4 pa-12">
                    Do you really want to enroll in
                    {{ currentCertification.name }} Certification?
                  </div>
                </v-card-text>
                <v-card-actions class="justify-end">
                  <div v-if="!snackbar">
                    <v-btn
                      color="secondary"
                      style="margin-right: 5px"
                      @click="dialog.value = false"
                      >Not yet!</v-btn
                    >
                    <v-btn
                      color="primary"
                      @click="createVoucher"
                      @certAdded="snackbar = true"
                      >Yes!</v-btn
                    >
                  </div>
                  <v-alert v-else type="success">
                    You successfully enrolled in
                    {{ currentCertification.name }} certification. Please wait
                    until your voucher gets activated.
                  </v-alert>
                </v-card-actions>
              </v-card>
            </template>
          </v-dialog>
        </v-card-actions>
      </v-card>
    </div>
    <div class="manager" v-else>
      <v-card class="mx-auto" width="600">
        <v-card-title>{{ currentCertification.name }}</v-card-title>
        <v-card-text class="text--primary">
          <div>
            URL:
            <a :href="currentCertification.url" target="_blank">{{
              currentCertification.url
            }}</a>
          </div>
          <div
            v-if="
              currentCertification.price == null ||
              currentCertification.price === 0
            "
          >
            Free
          </div>
          <div v-else>
            Price:
            {{ currentCertification.price }}
            {{ currentCertification.currency }}
          </div>
          <div v-for="skill in skills" :key="skill.id">
            <div>Skill: {{ skill.name }}</div>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn
            v-if="!edited && !deleted"
            @click="openEditCertificationDialog"
            color="primary"
            >Edit</v-btn
          >
          <v-btn
            v-if="!edited && !deleted"
            @click.stop="deleteDialog = true"
            :disabled="vouchersSize > 0"
            color="secondary"
            >Delete</v-btn
          >
          <v-dialog width="800" v-model="deleteDialog" persistent>
            <v-card>
              <v-card-title style="text-align: center">
                Do you really want to delete {{ currentCertification.name }} certification?
              </v-card-title>
              <v-card-text style="text-align: left">
                This operation cannot be undone.
              </v-card-text>
              <v-card-actions>
                <div v-if="!deleted">
                  <v-btn
                    style="margin-right: 5px"
                    @click="deleteCertification"
                    color="secondary"
                  >
                    Yes! Delete
                  </v-btn>
                  <v-btn @click.stop="deleteDialog = false" color="primary">
                    No!
                  </v-btn>
                </div>
                <v-alert v-else type="success">
                  {{ currentCertification.name }} successfully deleted.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog
            v-model="editCertificationDialog"
            persistent
            max-width="600px"
          >
            <v-card>
              <v-card-title> Edit certification </v-card-title>
              <v-card-text>
                <v-form v-model="valid">
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        v-model="changedName"
                        label="Edit name"
                        :rules="nameRules"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="6">
                      <v-text-field
                        v-model="changedPrice"
                        label="Edit price"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="6">
                      <v-text-field
                        v-model="changedCurrency"
                        label="Edit currency"
                        :rules="currencyRules"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        v-model="changedUrl"
                        label="Edit url"
                        :rules="urlRules"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-select
                        v-model="chosenSkillNames"
                        :items="allSkillNames"
                        label="Select skills"
                        outlined
                        multiple
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <div v-if="!edited">
                  <v-btn
                    style="margin-right: 5px"
                    @click="closeEditCertificationDialog"
                    color="secondary"
                  >
                    Close
                  </v-btn>
                  <v-btn
                    @click="editCertification"
                    :disabled="
                      !valid ||
                      (cmpSkills &&
                        changedName === currentCertification.name &&
                        changedUrl === currentCertification.url &&
                        changedPrice === currentCertification.price &&
                        changedCurrency === currentCertification.currency)
                    "
                    color="primary"
                  >
                    Edit
                  </v-btn>
                </div>
                <v-alert v-else type="success">
                  Certification successfully updated.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-card-actions>
      </v-card>
    </div>

    <v-row style="width: 100%">
      <v-col cols="12">
        <div
          v-if="reviews !== undefined && reviews.length > 0"
          class="card-wrapper"
        >
          <h1>Certification Reviews</h1>
          <v-row class="d-flex justify-center mb-6">
            <div v-for="review in reviews" :key="review.id" tag="div">
              <v-col>
                <ReviewItem :key="review.id" :review="review" />
              </v-col>
            </div>
          </v-row>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import axios from "axios";
import ReviewItem from "@/components/ReviewItem.vue";

export default {
  name: "CertificationDetail",
  components: {
    ReviewItem,
  },
  data() {
    return {
      localStorage,
      currentCertification: {},
      newCertificationDialog: false,
      nameRules: [v => !!v || "Name is required"],
      currencyRules: [v => !!v || "Currency is required"],
      urlRules: [v => !!v || "URL is required"],
      snackbar: false,
      fail: false,
      success: false,
      changedName: "",
      changedPrice: 0,
      changedCurrency: "",
      changedUrl: "",
      editCertificationDialog: false,
      valid: true,
      edited: false,
      deleted: false,
      vouchers: {},
      reviews: {},
      delError: false,
      vouchersSize: {},
      studentOwns: false,
      skills: {},
      skillNames: [],
      allSkills: {},
      allSkillNames: [],
      chosenSkillNames: [],
      skillsUnchanged: true,
      deleteDialog: false
    };
  },
  computed: {
    ...mapGetters(["certifications", "user", "deleteError"]),
    cmpSkills: function() {
      if (this.skillNames.length !== this.chosenSkillNames.length) return false;
      const skills = this.skillNames;
      const chosen = this.chosenSkillNames;
      skills.sort();
      chosen.sort();
      for (let i = 0; i < skills.length; i++) {
        if (skills[i] !== chosen[i]) return false;
      }
      return true;
    }
  },
  methods: {
    createVoucher() {
      this.$store.dispatch("createVoucherRequest", {
        state: "PENDING",
        voucherCode: "VC",
        userId: this.user.id,
        certId: this.currentCertification.id
      });
      this.snackbar = true;
      this.success = true;
      setTimeout(() => this.$router.push("/"), 3000);
    },
    deleteCertification() {
      this.$store.dispatch("createDeleteCertificationRequest", {
        id: this.currentCertification.id,
      });
      this.deleted = true;
      setTimeout(() => this.$router.push("/"), 1500);
    },
    openEditCertificationDialog() {
      this.editCertificationDialog = true;
      this.setEditCertificationDialog();
    },
    closeEditCertificationDialog() {
      console.log(this.chosenSkillNames);
      console.log(this.skillNames);
      this.editCertificationDialog = false;
      this.setEditCertificationDialog();
    },
    setEditCertificationDialog() {
      this.changedName = this.currentCertification.name;
      if (
        this.currentCertification.price === 0 ||
        this.currentCertification.price === null
      ) {
        this.currentCertification.price = 0;
        this.changedPrice = 0;
      } else this.changedPrice = this.currentCertification.price;
      this.changedCurrency = this.currentCertification.currency;
      this.changedUrl = this.currentCertification.url;
    },
    async editCertification() {
      await this.$store.dispatch("createEditCertification", {
        id: this.currentCertification.id,
        name: this.changedName,
        url: this.changedUrl,
        price: this.changedPrice,
        currency: this.changedCurrency,
      });
      await this.$store.dispatch("createDeleteSkillsRequest", {
        id: this.currentCertification.id
      });
      console.log("deleted");
      const ids = [];
      console.log(this.chosenSkillNames);
      this.chosenSkillNames.forEach(chosenSkillName => {
        this.allSkills.forEach(skill => {
          if (chosenSkillName === skill.name) ids.push(skill.id);
        });
      });
      for (let i = 0; i < ids.length; i++) {
        await this.$store.dispatch("createSkillToCertRequest", {
          id: this.currentCertification.id,
          skillId: ids[i]
        });
      }
      this.edited = true;
      setTimeout(() => this.$router.push("/"), 1500);
    }
  },
  async mounted() {
    const { data } = await axios.get(
      "http://localhost:8080/certifications/" + this.$route.params.id
    );
    this.currentCertification = data;
    await axios
      .get(
        "http://localhost:8080/certifications/" +
          this.currentCertification.id +
          "/vouchers"
      )
      .then(
        response =>
          (this.vouchersSize = response.data._embedded.vouchers.length)
      );
    console.log("id of this user");
    console.log(this.localStorage.getItem("userId"));
    console.log("Students that have vouchers for this certifications");
    await axios
      .get(
        "http://localhost:8080/certs/students/" + this.currentCertification.id
      )
      .then(response => {
        this.studentOwns = response.data.includes(
          parseInt(this.localStorage.getItem("userId"))
        );
      });
    await axios
      .get(
        "http://localhost:8080/certifications/" +
          this.currentCertification.id +
          "/skills"
      )
      .then(response => (this.skills = response.data._embedded.skills));
    await axios
      .get("http://localhost:8080/skills")
      .then(response => (this.allSkills = response.data._embedded.skills));
    this.allSkills.forEach(skill => this.allSkillNames.push(skill.name));
    this.skills.forEach(skill => this.chosenSkillNames.push(skill.name));
    this.skills.forEach(skill => this.skillNames.push(skill.name));
    console.log(this.chosenSkillNames);
    console.log(this.skillNames);

    const urlReview = "http://localhost:8080/reviews/" + this.currentCertification.id;
    const data2 = await axios.get(urlReview);
    this.reviews = data2.data;
  }
};
</script>

<style scoped>
.certification {
  padding: 50px;
  text-align: left;
  margin-top: 50px;
  position: center;
}

.post-detail__left {
  float: left;
  width: 50%;
  position: relative;
  min-height: 1px;
  padding-left: 25px;
  padding-right: 25px;
}

.post-detail__right {
  float: right;
  width: 50%;
  position: relative;
  min-height: 1px;
  padding-left: 25px;
  padding-right: 25px;
}
</style>

