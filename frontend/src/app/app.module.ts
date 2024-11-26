import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
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
import { FooterComponent } from './components/footer/footer.component';
import { HeaderInternalComponent } from './components/header-internal/header-internal.component';
import { HeaderComponent } from './components/header/header.component';
import { HomeInternalComponent } from './components/home-internal/home-internal.component';
import { HomeComponent } from './components/home/home.component';
import { JournalistsBecomeComponent } from './components/journalists-become/journalists-become.component';
import { JournalistsDetailsComponent } from './components/journalists-details/journalists-details.component';
import { JournalistsUnverifiedComponent } from './components/journalists-unverified/journalists-unverified.component';
import { JournalistsVerifiedComponent } from './components/journalists-verified/journalists-verified.component';
import { JournalistsComponent } from './components/journalists/journalists.component';
import { LoginComponent } from './components/login/login.component';
import { NotificationBannerComponent } from './components/notification-banner/notification-banner.component';
import { PostingOverviewComponent } from './components/posting-overview/posting-overview.component';
import { RegisterComponent } from './components/register/register.component';
import { TopicDetailsComponent } from './components/topic-details/topic-details.component';
import { httpInterceptorProviders } from './interceptor';
import { CommentaryComponent } from './components/commentary/commentary.component';
import { PostComponent } from './components/post/post.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { PostFeedComponent } from './components/post-feed/post-feed.component';
import { JournalistsRequestsComponent } from './components/journalists-requests/journalists-requests.component';
import { HeaderFeedComponent } from './components/header-feed/header-feed.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    Error404Component,
    LoginComponent,
    FooterComponent,
    RegisterComponent,
    HeaderInternalComponent,
    HomeInternalComponent,
    AccountComponent,
    AffiliationsComponent,
    AffiliationsDetailsComponent,
    AffiliationsCreateComponent,
    AffiliationsUnverifiedComponent,
    AffiliationsVerifiedComponent,
    JournalistsComponent,
    JournalistsDetailsComponent,
    JournalistsBecomeComponent,
    JournalistsVerifiedComponent,
    JournalistsUnverifiedComponent,
    ContactComponent,
    CertificatesComponent,
    CertificatesCreateComponent,
    CertificatesListComponent,
    TopicDetailsComponent,
    NotificationBannerComponent,
    PostFeedComponent,
    NotificationBannerComponent,
    PostingOverviewComponent,
    CommentaryComponent,
    PostComponent,
    JournalistsRequestsComponent,
    HeaderFeedComponent
  ],
  imports: [
    BrowserModule,
    RouterModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule,
    CommonModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    InfiniteScrollModule,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent],
})
export class AppModule {}
