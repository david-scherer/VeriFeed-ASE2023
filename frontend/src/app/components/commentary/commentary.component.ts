import { Component, Input, OnInit } from '@angular/core';
import { Commentary } from '../../model/commentary';
import { PostingService } from '../../service/posting.service';
import { AuthenticationService } from '../../service/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-commentary',
  templateUrl: './commentary.component.html',
  styleUrls: ['./commentary.component.scss'],
})
export class CommentaryComponent implements OnInit {
  @Input({ required: true }) commentary!: Commentary;

  deleted = false;
  date: Date = new Date();
  editMode = false;
  isLoggedIn = false;
  canEdit = false;
  backup = '';

  constructor(
    private postingService: PostingService,
    private authenticationService: AuthenticationService,
    private router: Router,
  ) {}

  ngOnInit() {
    let dateString = this.commentary.published.toString();

    // Split the string into components
    let parts = dateString.split(',');

    // Adjust month by subtracting 1 (JavaScript months are 0-based)
    let month = Number(parts[1]) - 1;

    // Create a new Date object
    let commDate = new Date(
      Number(parts[0]),
      month,
      Number(parts[2]),
      Number(parts[3]),
      Number(parts[4]),
      Number(parts[5]),
      Number(parts[6]) / 1000000,
    );

    this.date = commDate;

    this.canEdit =
      this.commentary.publishedBy.id ===
      this.authenticationService.getLoggedInUserId();

    this.isLoggedIn = this.authenticationService.isLoggedIn();
  }

  deleteCommentary() {
    this.postingService
      .deleteCommentary(this.commentary.relatedPost.id, this.commentary.id)
      .subscribe({
        next: () => {
          this.deleted = true;
        },
      });
  }

  upvoteCommentary() {
    if (this.isLoggedIn) {
      this.postingService
        .upvoteCommentary(this.commentary.relatedPost.id, this.commentary.id)
        .subscribe({
          next: () => {
            console.log('upvoted');
          },
        });

      if (!this.commentary.vote) {
        this.commentary.vote = 0;
      }
      this.commentary.score -= this.commentary.vote;
      this.commentary.score += 1;
      this.commentary.vote = 1;
    } else {
      this.redirectToLogin();
    }
  }

  downvoteCommentary() {
    if (this.isLoggedIn) {
      this.postingService
        .downvoteCommentary(this.commentary.relatedPost.id, this.commentary.id)
        .subscribe({
          next: () => {
            console.log('downvoted');
          },
        });

      if (!this.commentary.vote) {
        this.commentary.vote = 0;
      }
      this.commentary.score -= this.commentary.vote;
      this.commentary.score -= 1;
      this.commentary.vote = -1;
    } else {
      this.redirectToLogin();
    }
  }

  unvoteCommentary() {
    if (this.isLoggedIn) {
      this.postingService
        .unvoteCommentary(this.commentary.relatedPost.id, this.commentary.id)
        .subscribe({
          next: () => {
            console.log('unvoted');
          },
        });

      this.commentary.score -= this.commentary.vote;
      this.commentary.vote = 0;
    } else {
      this.redirectToLogin();
    }
  }

  editComment() {
    this.commentary.version++;
    this.editMode = false;
    this.postingService
      .editCommentary(this.commentary.relatedPost.id, this.commentary.id, {
        text: this.commentary.text,
      })
      .subscribe({
        next: () => {
          console.log('edited');
        },
      });
  }

  startEdit() {
    this.backup = this.commentary.text;
    this.editMode = true;
  }

  cancelEdit() {
    this.commentary.text = this.backup;
    this.editMode = false;
  }

  private redirectToLogin() {
    this.router.navigate(['/login']);
  }

  searchJournalist(journalistId: string) {
    this.router.navigate([`/journalists/${journalistId}`])
  }

  getProfileImg() {
    if (this.commentary.publishedBy?.img) {
      return this.commentary.publishedBy?.img;
    }
    return 'assets/user.png';
  }
}
