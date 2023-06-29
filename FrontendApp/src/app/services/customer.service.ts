import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../../model/customer.model";
import {environment} from "../../environments/environment";
import {CustomerAccount} from "../../model/customerAccount.model";


@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http: HttpClient) {

  }
  public getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>("http://localhost:8085/customers")
  }
  public searchCustomers(keyword : String): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>("http://localhost:8085/customers/search?keyword="+keyword)
  }
  public saveCustomer(customer : Customer): Observable<Customer> {
    return this.http.post<Customer>("http://localhost:8085/customers" , customer);
  }
  public deleteCustomer(id : number) {
    return this.http.delete<Customer>("http://localhost:8085/customers/"+id );
  }
  public customerAccount(customerId : number ,  page : number , size : number ):Observable<CustomerAccount>{
    return this.http.get<CustomerAccount>("http://localhost:8085/customer/accounts/"+customerId+"?page="+page+"&size="+size );
  }

}
