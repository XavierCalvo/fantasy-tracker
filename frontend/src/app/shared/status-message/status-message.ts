import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export type StatusMessageMode = 'loading' | 'error' | 'empty';

@Component({
  imports: [MatProgressSpinnerModule],
  selector: 'app-status-message',
  styleUrl: './status-message.scss',
  templateUrl: './status-message.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class StatusMessage {
  readonly mode = input.required<StatusMessageMode>();
  readonly message = input.required<string>();
}
