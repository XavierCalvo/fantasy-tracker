import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StatusMessage } from './status-message';

describe('StatusMessage', () => {
  let fixture: ComponentFixture<StatusMessage>;

  async function render(mode: 'loading' | 'error' | 'empty', message: string) {
    await TestBed.configureTestingModule({
      imports: [StatusMessage],
    }).compileComponents();

    fixture = TestBed.createComponent(StatusMessage);
    fixture.componentRef.setInput('mode', mode);
    fixture.componentRef.setInput('message', message);
    fixture.detectChanges();
  }

  it('should create', async () => {
    await render('loading', 'Cargando...');
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('should show a spinner while loading', async () => {
    await render('loading', 'Cargando...');
    expect(fixture.nativeElement.querySelector('mat-spinner')).toBeTruthy();
    expect(fixture.nativeElement.textContent).toContain('Cargando...');
  });

  it('should style the error state', async () => {
    await render('error', 'Algo falló');
    const errorEl = fixture.nativeElement.querySelector('.status-message--error');
    expect(errorEl).toBeTruthy();
    expect(errorEl.textContent).toContain('Algo falló');
  });

  it('should show a plain message for the empty state', async () => {
    await render('empty', 'No hay resultados');
    expect(fixture.nativeElement.querySelector('mat-spinner')).toBeFalsy();
    expect(fixture.nativeElement.textContent).toContain('No hay resultados');
  });
});
