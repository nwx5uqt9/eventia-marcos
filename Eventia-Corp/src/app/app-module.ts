import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import RegisterPage from './shared/pages/register-page/register-page';
import { App } from './app';
import { FormAddUser } from './admin/components/forms/form-add-user/form-add-user';



@NgModule({
  imports: [
    RegisterPage,
    CommonModule,
    HttpClientModule,
    FormsModule
    
  ],

})
export class AppModule { }
