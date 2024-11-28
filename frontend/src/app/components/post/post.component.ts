import { Component, Input, OnInit } from '@angular/core';
import { Posting } from '../../model/posting';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Commentary } from '../../model/commentary';
import { PostingService } from '../../service/posting.service';
import { AuthenticationService } from '../../service/authentication.service';
import { UserRoles } from '../../guards/user-roles';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss'],
})
export class PostComponent implements OnInit {
  @Input({ required: true }) posting!: Posting;

  commentaries: Commentary[] = [];
  newComment = '';
  isJournalist = false;
  isAdmin = false;
  displayLimit = 2;
  sortMode: 'score' | 'newest' = 'score';

  url: SafeResourceUrl = '';
  deleted =  false;

  constructor(
    private sanitizer: DomSanitizer,
    private postingService: PostingService,
    private authenticationService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  getTrustedEmbedUrl(url: URL): SafeResourceUrl {

    if (url.toString().includes('https://www.reddit.com/')) {
      // Replace 'www.reddit.com' with 'embed.reddit.com' in the URL
      let embedUrl = url.toString().replace('www.reddit.com', 'embed.reddit.com');

      // Add the necessary query parameters for embedding
      const queryParams = '?embed=true&ref_source=embed&ref=share&utm_medium=widgets&utm_source=embedv2&utm_term=23&utm_name=post_embed&embed_host_url=https%3A%2F%2Frebed.redditmedia.com%2Fembed';
      embedUrl += queryParams;

      return this.sanitizer.bypassSecurityTrustResourceUrl(
        embedUrl
      );
    }

    return this.sanitizer.bypassSecurityTrustResourceUrl(
      url.toString() + '/embed',
    );
  }

  postComment() {
    this.postingService
      .postCommentary(this.posting.id, { text: this.newComment })
      .subscribe({
        next: (res) => {
          this.newComment = '';
          this.commentaries.push(res);
          this.sortCommentaries();
        },
        error: (err) => {
          console.log(err);
        },
      });
  }

  ngOnInit(): void {
    this.postingService.getCommentaries(this.posting.id).subscribe({
      next: (res) => {
        this.commentaries = res;
        this.sortCommentaries();
      },
    });
    this.isAdmin =
      this.authenticationService.isLoggedIn() &&
      this.authenticationService.getUserRole() == UserRoles.ADMIN;
    this.isJournalist =
      this.authenticationService.isLoggedIn() &&
      this.authenticationService.getUserRole() == UserRoles.JOURNALIST;

    for (let source of this.posting.sources) {
      this.url = this.getTrustedEmbedUrl(source.location);
    }
  }

  loadMoreCommentaries() {
    this.displayLimit += 5;
  }

  changeSortMode(newSortMode: 'score' | 'newest') {
    this.sortMode = newSortMode;
    this.sortCommentaries();
  }

  private sortCommentaries() {
    switch (this.sortMode) {
      case 'newest':
        this.commentaries.sort((a, b) => {
          if (
            this.toDate(b.published.toString()) >
            this.toDate(a.published.toString())
          ) {
            return 1;
          } else {
            return -1;
          }
        });
        break;
      case 'score':
        this.commentaries.sort((a, b) => b.score - a.score);
    }
  }

  private toDate(s: string) {
    // Split the string into components
    let parts = s.split(',');

    // Adjust month by subtracting 1 (JavaScript months are 0-based)
    let month = Number(parts[1]) - 1;

    // Create a new Date object
    return new Date(
      Number(parts[0]),
      month,
      Number(parts[2]),
      Number(parts[3]),
      Number(parts[4]),
      Number(parts[5]),
      Number(parts[6]) / 1000000,
    );
  }

  searchTopic() {
    this.router.navigate([], {
      queryParams: { q: this.posting.topic.name },
      queryParamsHandling: 'merge',
      relativeTo: this.route
    });
  }

  blockPost() {
    if (window.confirm("Posting will be deleted and blacklisted")) {
      this.deleted = true;
      this.postingService.blockPost(this.posting.id).subscribe({
        error(err) {
          console.log(err);
        }
      })
    }
  }
}
