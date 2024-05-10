export class Patient {
    id?: any;
    patientId?: string;
    firstName?: string;
    lastName?: string;
    dob?: Date;
    address?: string;
    phoneNumber?: string;
}

export interface PatientTable {
    patients: Patient[];
    currentPage: number;
    totalItems: number;
    totalPages: number;
}