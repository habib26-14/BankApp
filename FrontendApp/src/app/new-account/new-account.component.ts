import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AccountsService} from "../services/accounts.service";

@Component({
  selector: 'app-new-account',
  templateUrl: './new-account.component.html',
  styleUrls: ['./new-account.component.css']
})
export class NewAccountComponent implements OnInit{
  newAccountFormGroup! : FormGroup;
  customer! : Customer;
  ngOnInit(): void {
  }
  constructor(private route : ActivatedRoute , private router : Router , private fb : FormBuilder , private accountsService : AccountsService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;

    this.newAccountFormGroup=this.fb.group({
      accountType:fb.control(null),
      initialBalance:fb.control(0),
      interestRate:fb.control(0),
      overDreaft:fb.control(0),


    })
  }

  HandleSaveAccount() {
    let accountType : string =this.newAccountFormGroup.value.accountType
    let initialBalance : number = this.newAccountFormGroup.value.initialBalance
    let interestRate : number = this.newAccountFormGroup.value.interestRate
    let overDreaft : number = this.newAccountFormGroup.value.overDreaft
    if(accountType=='saving'){
        this.accountsService.saveSavingAccount(initialBalance,interestRate,this.customer.id).subscribe({
          next:(data)=>{
            alert("Account Created")
            this.newAccountFormGroup.reset()

          },
          error:(err)=>{
            console.log(err)

          }

        });
    }else if(accountType=='current'){
      this.accountsService.saveCurrentAccount(initialBalance,overDreaft,this.customer.id).subscribe({
        next:(data)=>{
          alert("Account Created")
          this.newAccountFormGroup.reset()
        },
        error:(err)=>{
          console.log(err)
        }

      });
    }

  }
}
