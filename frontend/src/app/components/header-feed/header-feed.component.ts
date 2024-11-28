import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-header-feed',
  templateUrl: './header-feed.component.html',
  styleUrls: ['./header-feed.component.scss']
})
export class HeaderFeedComponent implements OnInit{

  @Input() navBase: string = '';

  onlyCommented = true;

  query: string = "";

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((queryParams: Params) => {
      const q = queryParams['q'];
      if (q) {
        this.query = q;
      }

      if (queryParams['oc'] !== undefined) {
        const oc = Boolean(Number(queryParams['oc']));
        this.onlyCommented = oc;
      }
    })
  }

  search() {
    this.navigate();
  }


  turnOnCommentOnlyMode() {
    this.onlyCommented = true;
    this.navigate();
  }

  turnOffCommentOnlyMode() {
    this.onlyCommented = false;
    this.navigate();
  }

  navigate() {
    this.router.navigateByUrl(`/${this.navBase}?q=${this.query}&oc=${this.onlyCommented?'1':'0'}` );
  }
}
