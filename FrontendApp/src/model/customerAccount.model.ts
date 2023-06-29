export interface CustomerAccount {
  id:              number;
  name:            string;
  email:           string;
  currentPage:     number;
  totalPages:      number;
  pageSize:        number;
  bankAccountDTOS: BankAccountDTO[];
}

export interface BankAccountDTO {
  type:        string;
  id:          string;
  balance:     number;
  createAt:    Date;
  status:      null;
  customerDTO: CustomerDTO;
  overDraft?:  number;
  interesRate: number;
}

export interface CustomerDTO {
  id:    number;
  name:  string;
  email: string;
}
