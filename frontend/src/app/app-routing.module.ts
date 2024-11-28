import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from './components/account/account.component';
import { AffiliationsCreateComponent } from './components/affiliations-create/affiliations-create.component';
import { AffiliationsDetailsComponent } from './components/affiliations-details/affiliations-details.component';
import { AffiliationsUnverifiedComponent } from './components/affiliations-unverified/affiliations-unverified.component';
import { AffiliationsVerifiedComponent } from './components/affiliations-verified/affiliations-verified.component';
import { AffiliationsComponent } from './components/affiliations/affiliations.component';
import { CertificatesCreateComponent } from './components/certificates-create/certificates-create.component';
import { CertificatesListComponent } from './components/certificates-list/certificates-list.component';
import { CertificatesComponent } from './components/certificates/certificates.component';
import { ContactComponent } from './components/contact/contact.component';
import { Error404Component } from './components/error404/error404.component';
import { HomeInternalComponent } from './components/home-internal/home-internal.component';
import { HomeComponent } from './components/home/home.component';
import { JournalistsBecomeComponent } from './components/journalists-become/journalists-become.component';
import { JournalistsDetailsComponent } from './components/journalists-details/journalists-details.component';
import { JournalistsUnverifiedComponent } from './components/journalists-unverified/journalists-unverified.component';
import { JournalistsVerifiedComponent } from './components/journalists-verified/journalists-verified.component';
import { JournalistsComponent } from './components/journalists/journalists.component';
import { LoginComponent } from './components/login/login.component';
import { PostingOverviewComponent } from './components/posting-overview/posting-overview.component';
import { RegisterComponent } from './components/register/register.component';
import { TopicDetailsComponent } from './components/topic-details/topic-details.component';
import { AuthGuard } from './guards/auth.guard';
import { AuthGuardAdmin } from './guards/auth.guard.admin';
import { AuthGuardJournalist } from './guards/auth.guard.journalist';
import {JournalistsRequestsComponent} from './components/journalists-requests/journalists-requests.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '' },
  { path: '', component: HomeComponent },
  { path: '404', component: Error404Component },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'internal',
    canActivate: [AuthGuard],
    component: HomeInternalComponent,
  },
  { path: 'account', canActivate: [AuthGuard], component: AccountComponent },
  { path: 'topic', canActivate: [AuthGuard], component: TopicDetailsComponent },
  { path: 'contact', component: ContactComponent },
  {
    path: 'affiliations',
    canActivate: [AuthGuard],
    component: AffiliationsComponent,
    children: [
      {
        path: '',
        canActivate: [AuthGuard],
        component: AffiliationsVerifiedComponent,
      },
      {
        path: 'unverified',
        canActivate: [AuthGuardAdmin],
        component: AffiliationsUnverifiedComponent,
      },
      {
        path: 'create',
        canActivate: [AuthGuardJournalist],
        component: AffiliationsCreateComponent,
      },
      {
        path: ':id',
        canActivate: [AuthGuard],
        component: AffiliationsDetailsComponent,
      },
    ],
  },
  {
    path: 'journalists',
    canActivate: [AuthGuard],
    component: JournalistsComponent,
    children: [
      {
        path: '',
        canActivate: [AuthGuard],
        component: JournalistsVerifiedComponent,
      },
      {
        path: 'unverified',
        canActivate: [AuthGuardAdmin],
        component: JournalistsUnverifiedComponent,
      },
      { path: 'become', component: JournalistsBecomeComponent },
      {
        path: 'requests',
        canActivate: [AuthGuardJournalist],
        component: JournalistsRequestsComponent,
      },
      {
        path: ':id',
        canActivate: [AuthGuard],
        component: JournalistsDetailsComponent,
      },
    ],
  },
  {
    path: 'certificates',
    canActivate: [AuthGuardAdmin],
    component: CertificatesComponent,
    children: [
      {
        path: '',
        canActivate: [AuthGuardAdmin],
        component: CertificatesListComponent,
      },
      {
        path: 'create',
        canActivate: [AuthGuardAdmin],
        component: CertificatesCreateComponent,
      },
    ],
  },
  {
    path: 'postings',
    component: PostingOverviewComponent,
  },
  { path: '**', pathMatch: 'full', component: Error404Component },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
