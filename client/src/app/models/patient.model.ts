export interface Patient {
    id?: any;
    patientId?: string;
    firstName?: string;
    lastName?: string;
    dob?: Date;
    address?: string;
    phoneNumber?: string;
}

export interface PatientTable {
    data: Patient[];
    page: number;
    per_page: number;
    total: number;
    total_pages: number;
}