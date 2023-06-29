import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../../model/customer.model";
import {catchError, Observable, throwError} from "rxjs";
import {CustomerService} from "../services/customer.service";
import {CustomerAccount} from "../../model/customerAccount.model";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent implements OnInit{
  customerId! : number ;
  customer! : Customer;
  currentPage : number=0;
  pageSize : number=5;
  accountObservable! : Observable<CustomerAccount>;
  errorMessage! :string;
  constructor(private route : ActivatedRoute , private router : Router  , private customerService : CustomerService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
    this.customerId=this.route.snapshot.params['id']

  }
  ngOnInit(): void {
   this.loadAccounts();
  }
  goToPage(page: number) {
    this.currentPage=page;
    this.loadAccounts();
  }
  loadAccounts():void{
    let id=this.customerId;
    this.accountObservable=this.customerService.customerAccount(id, this.currentPage, this.pageSize).pipe(
      catchError(err=>{
        this.errorMessage=err.message
        return throwError(err);

      })
    )
  }



}
