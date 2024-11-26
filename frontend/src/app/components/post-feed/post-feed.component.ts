import { Component, Input, OnInit } from '@angular/core';
import { PostingService } from '../../service/posting.service';
import { Posting } from '../../model/posting';
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'app-post-feed',
  templateUrl: './post-feed.component.html',
  styleUrls: ['./post-feed.component.scss'],
})
export class PostFeedComponent implements OnInit {
  onlyCommented: boolean = true;
  dispayedPosts: Posting[] = [];
  posts: Posting[] = [];
  scrollDistance = 2;
  scrollUpDistance = 2;
  throttle = 300;
  done = false;

  currentFeedSize = 5;
  readyPosts = 0;
  query = '';

  loading = true;

  constructor(private postingService: PostingService, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.appendItems(0, this.currentFeedSize);

    this.activatedRoute.queryParams.subscribe((queryParams: Params) => {
      const q = queryParams['q'];
      if (q) {
        this.query = q;
      } else {
        this.query = '';
      }


      if (queryParams['oc'] !== undefined) {
        console.log(queryParams['oc'])
        const oc = Boolean(Number(queryParams['oc']));
        this.onlyCommented = oc;
      }

      this.reloadPosts();
    })
  }

  reloadPosts() {
    window.scrollTo(0, 0);
    this.loading = true;

    this.dispayedPosts = [];
    this.posts = [];
    this.done = false;
    this.currentFeedSize = 5;
    this.readyPosts = 0;


    this.postingService.getPosts(0, 20, this.onlyCommented, this.query).subscribe({
      next: (res) => {
        this.posts = res;
        this.readyPosts = 20;
        this.appendItems(0, this.currentFeedSize);

        this.loading = false;
      }
    });
  }

  onScrollDown() {
    this.appendItems(this.currentFeedSize, this.currentFeedSize + 5);
  }

  private appendItems(startIndex: number, endIndex: number) {
    for (let i = startIndex; i < endIndex; i++) {
      let posting: Posting | undefined = this.posts.pop();
      if (this.posts.length == 10) {
        this.refreshPosts();
      }

      if (posting) {
        this.dispayedPosts.push(posting);
      }
    }
  }

  private refreshPosts() {
    if (!this.done) {
      this.postingService
        .getPosts(this.readyPosts, 20, this.onlyCommented, this.query)
        .subscribe({
          next: (res: Posting[]) => {
            if (res.length == 0) {
              this.done = true;
            }
            this.readyPosts += res.length;
            for (let posting of res) {
              this.posts.push(posting);
            }
          }
        });
    }
  }
}
