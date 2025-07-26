import axios from "axios";

const API_BASE_URL = "http://localhost:8085";

// Create axios instance
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor to add token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// Health Check
export const healthCheck = () => api.get("/health");

// Patient APIs
export const patientAPI = {
  register: (patientData) => api.post("/patient/register", patientData),
  login: (credentials) => api.post("/patient/login", credentials),
  getProfile: (token) => api.get(`/patient/me/${token}`),
  getAppointments: (token) => api.get(`/patient/appointments/${token}`),
  filterAppointmentsByCondition: (token, condition) =>
    api.get(`/patient/appointments/filter/condition/${token}/${condition}`),
  filterAppointmentsByDoctor: (token, doctorName) =>
    api.get(`/patient/appointments/filter/doctor/${token}/${doctorName}`),
  filterByDoctorAndCondition: (token, doctorName, condition) =>
    api.get(`/patient/appointments/filter/${token}/${doctorName}/${condition}`),
};

// Doctor APIs
export const doctorAPI = {
  register: (doctorData) => api.post("/doctor/register", doctorData),
  login: (credentials) => api.post("/doctor/login", credentials),
  getAll: () => api.get("/doctor/all"),
  getById: (id) => api.get(`/doctor/${id}`),
  update: (id, doctorData) => api.put(`/doctor/${id}`, doctorData),
  delete: (id) => api.delete(`/doctor/${id}`),
  getAppointments: (token) => api.get(`/doctor/appointments/${token}`),
  updateAppointment: (id, appointmentData) =>
    api.put(`/appointment/${id}`, appointmentData),
};

// Admin APIs
export const adminAPI = {
  login: (credentials) => api.post("/admin/login", credentials),
  getAllPatients: () => api.get("/admin/patients"),
  getAllDoctors: () => api.get("/admin/doctors"),
  getAllAppointments: () => api.get("/admin/appointments"),
  deletePatient: (id) => api.delete(`/admin/patient/${id}`),
  deleteDoctor: (id) => api.delete(`/admin/doctor/${id}`),
  deleteAppointment: (id) => api.delete(`/admin/appointment/${id}`),
};

// Appointment APIs
export const appointmentAPI = {
  create: (appointmentData) => api.post("/appointment/create", appointmentData),
  getAll: () => api.get("/appointment/all"),
  getById: (id) => api.get(`/appointment/${id}`),
  update: (id, appointmentData) =>
    api.put(`/appointment/${id}`, appointmentData),
  delete: (id) => api.delete(`/appointment/${id}`),
  getByPatient: (patientId) => api.get(`/appointment/patient/${patientId}`),
  getByDoctor: (doctorId) => api.get(`/appointment/doctor/${doctorId}`),
};

// Prescription APIs
export const prescriptionAPI = {
  create: (prescriptionData) =>
    api.post("/prescription/create", prescriptionData),
  getAll: () => api.get("/prescription/all"),
  getById: (id) => api.get(`/prescription/${id}`),
  update: (id, prescriptionData) =>
    api.put(`/prescription/${id}`, prescriptionData),
  delete: (id) => api.delete(`/prescription/${id}`),
  getByPatient: (patientId) => api.get(`/prescription/patient/${patientId}`),
  getByDoctor: (doctorId) => api.get(`/prescription/doctor/${doctorId}`),
};

export default api;
