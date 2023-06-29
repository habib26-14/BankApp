import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Customer} from "../../model/customer.model";
import {Observable} from "rxjs";
import {AccountDetails} from "../../model/account.model";
import {AccountsComponent} from "../accounts/accounts.component";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http : HttpClient) { }

  public getAccount(accountId : string ,  page : number , size : number ):Observable<AccountDetails>{
    return this.http.get<AccountDetails>("http://localhost:8085/accounts/"+accountId+"/pageOperations?page="+page+"&size="+size );
  }
  public debit(accountId : string ,  amount : number , description : string ){
    let data={accountId:accountId,amount:amount ,description:description };
    return this.http.post("http://localhost:8085/accounts/debit",data);
  }
  public credit(accountId : string ,  amount : number , description : string ){
    let data={accountId:accountId,amount:amount ,description:description };
    return this.http.post("http://localhost:8085/accounts/credit",data);
  }
  public transfer(accountSource : string , accountDestination: string ,  amount : number , description : string ){
    let data={accountSource,accountDestination,amount ,description };
    return this.http.post("http://localhost:8085/accounts/transfer",data);
  }
  public saveSavingAccount( initialBalance :number ,  interestRate :number,  customerId:number ){
    let data={initialBalance,interestRate,customerId };
    return this.http.post("http://localhost:8085/account/saveSavingAccount",data);
  }
  public saveCurrentAccount( initialBalance :number ,  overDreaft :number,  customerId:number ){
    let data={initialBalance,overDreaft,customerId };
    return this.http.post("http://localhost:8085/account/saveCurrentAccount",data);
  }
}
