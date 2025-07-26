import React, { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { doctorAPI, appointmentAPI, prescriptionAPI } from "../services/api";
import { toast } from "react-hot-toast";
import {
  Calendar,
  Clock,
  User,
  Stethoscope,
  Plus,
  Edit,
  LogOut,
  User as UserIcon,
  FileText,
} from "lucide-react";

const DoctorDashboard = () => {
  const { user, logout } = useAuth();
  const [appointments, setAppointments] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showNewPrescription, setShowNewPrescription] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState("");
  const [prescriptionText, setPrescriptionText] = useState("");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [appointmentsRes, prescriptionsRes] = await Promise.all([
        doctorAPI.getAppointments(user.token),
        prescriptionAPI.getByDoctor(user.id),
      ]);

      setAppointments(appointmentsRes.data || []);
      setPrescriptions(prescriptionsRes.data || []);
    } catch (error) {
      toast.error("Failed to load data");
    } finally {
      setLoading(false);
    }
  };

  const handleCreatePrescription = async (e) => {
    e.preventDefault();

    if (!selectedAppointment || !prescriptionText) {
      toast.error("Please fill in all fields");
      return;
    }

    try {
      const prescriptionData = {
        appointmentId: selectedAppointment,
        doctorId: user.id,
        prescriptionText: prescriptionText,
        date: new Date().toISOString(),
      };

      await prescriptionAPI.create(prescriptionData);
      toast.success("Prescription created successfully!");
      setShowNewPrescription(false);
      setPrescriptionText("");
      setSelectedAppointment("");
      loadData();
    } catch (error) {
      toast.error("Failed to create prescription");
    }
  };

  const handleUpdateAppointmentStatus = async (appointmentId, newStatus) => {
    try {
      await doctorAPI.updateAppointment(appointmentId, { status: newStatus });
      toast.success("Appointment status updated!");
      loadData();
    } catch (error) {
      toast.error("Failed to update appointment status");
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case 0:
        return "Pending";
      case 1:
        return "Completed";
      case 2:
        return "Cancelled";
      default:
        return "Unknown";
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 0:
        return "bg-yellow-100 text-yellow-800";
      case 1:
        return "bg-green-100 text-green-800";
      case 2:
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <Stethoscope className="h-8 w-8 text-primary-600 mr-3" />
              <h1 className="text-2xl font-bold text-gray-900">
                Doctor Dashboard
              </h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-gray-700">Dr. {user.name}</span>
              <button
                onClick={logout}
                className="flex items-center px-3 py-2 text-sm font-medium text-gray-700 hover:text-gray-900"
              >
                <LogOut className="h-4 w-4 mr-1" />
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <Calendar className="h-6 w-6 text-primary-600" />
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      Total Appointments
                    </dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {appointments.length}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <Clock className="h-6 w-6 text-yellow-600" />
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      Pending
                    </dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {appointments.filter((a) => a.status === 0).length}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="p-5">
              <div className="flex items-center">
                <FileText className="h-6 w-6 text-green-600" />
                <div className="ml-5 w-0 flex-1">
                  <dl>
                    <dt className="text-sm font-medium text-gray-500 truncate">
                      Prescriptions
                    </dt>
                    <dd className="text-lg font-medium text-gray-900">
                      {prescriptions.length}
                    </dd>
                  </dl>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="bg-white shadow rounded-lg mb-8">
          <div className="px-4 py-5 sm:p-6">
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-medium text-gray-900">
                Today's Appointments
              </h3>
              <button
                onClick={() => setShowNewPrescription(true)}
                className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-primary-600 hover:bg-primary-700"
              >
                <Plus className="h-4 w-4 mr-2" />
                New Prescription
              </button>
            </div>
          </div>
        </div>

        {/* Appointments List */}
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {appointments.length === 0 ? (
              <li className="px-6 py-4 text-center text-gray-500">
                No appointments found
              </li>
            ) : (
              appointments.map((appointment) => (
                <li key={appointment.id} className="px-6 py-4">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center">
                      <div className="flex-shrink-0">
                        <User className="h-8 w-8 text-gray-400" />
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          Patient:{" "}
                          {appointment.patient?.name || "Unknown Patient"}
                        </div>
                        <div className="text-sm text-gray-500">
                          {new Date(
                            appointment.appointmentTime
                          ).toLocaleString()}
                        </div>
                        <div className="text-sm text-gray-500">
                          Phone: {appointment.patient?.phone || "N/A"}
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center space-x-4">
                      <span
                        className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${getStatusColor(
                          appointment.status
                        )}`}
                      >
                        {getStatusText(appointment.status)}
                      </span>

                      {appointment.status === 0 && (
                        <div className="flex space-x-2">
                          <button
                            onClick={() =>
                              handleUpdateAppointmentStatus(appointment.id, 1)
                            }
                            className="inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded text-green-700 bg-green-100 hover:bg-green-200"
                          >
                            Complete
                          </button>
                          <button
                            onClick={() =>
                              handleUpdateAppointmentStatus(appointment.id, 2)
                            }
                            className="inline-flex items-center px-2 py-1 border border-transparent text-xs font-medium rounded text-red-700 bg-red-100 hover:bg-red-200"
                          >
                            Cancel
                          </button>
                        </div>
                      )}
                    </div>
                  </div>
                </li>
              ))
            )}
          </ul>
        </div>

        {/* Prescriptions Section */}
        <div className="mt-8 bg-white shadow overflow-hidden sm:rounded-md">
          <div className="px-4 py-5 sm:px-6">
            <h3 className="text-lg font-medium text-gray-900">
              Recent Prescriptions
            </h3>
          </div>
          <ul className="divide-y divide-gray-200">
            {prescriptions.length === 0 ? (
              <li className="px-6 py-4 text-center text-gray-500">
                No prescriptions found
              </li>
            ) : (
              prescriptions.slice(0, 5).map((prescription) => (
                <li key={prescription.id} className="px-6 py-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <div className="text-sm font-medium text-gray-900">
                        Patient: {prescription.patient?.name || "Unknown"}
                      </div>
                      <div className="text-sm text-gray-500">
                        {new Date(prescription.date).toLocaleDateString()}
                      </div>
                      <div className="text-sm text-gray-600 mt-1">
                        {prescription.prescriptionText}
                      </div>
                    </div>
                  </div>
                </li>
              ))
            )}
          </ul>
        </div>
      </div>

      {/* New Prescription Modal */}
      {showNewPrescription && (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
          <div className="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
            <div className="mt-3">
              <h3 className="text-lg font-medium text-gray-900 mb-4">
                New Prescription
              </h3>
              <form onSubmit={handleCreatePrescription} className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Appointment
                  </label>
                  <select
                    value={selectedAppointment}
                    onChange={(e) => setSelectedAppointment(e.target.value)}
                    className="mt-1 block w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
                    required
                  >
                    <option value="">Select an appointment</option>
                    {appointments
                      .filter((a) => a.status === 1)
                      .map((appointment) => (
                        <option key={appointment.id} value={appointment.id}>
                          {appointment.patient?.name} -{" "}
                          {new Date(
                            appointment.appointmentTime
                          ).toLocaleDateString()}
                        </option>
                      ))}
                  </select>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700">
                    Prescription
                  </label>
                  <textarea
                    value={prescriptionText}
                    onChange={(e) => setPrescriptionText(e.target.value)}
                    rows={4}
                    className="mt-1 block w-full border border-gray-300 rounded-md px-3 py-2 text-sm"
                    placeholder="Enter prescription details..."
                    required
                  />
                </div>

                <div className="flex justify-end space-x-3">
                  <button
                    type="button"
                    onClick={() => setShowNewPrescription(false)}
                    className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-4 py-2 text-sm font-medium text-white bg-primary-600 rounded-md hover:bg-primary-700"
                  >
                    Create
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorDashboard;
